package com.nightletter.domain.tarot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.diary.dto.EmbedVector;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarot;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.tarot.dto.TarotResponse;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.dto.TarotKeyword;
import com.nightletter.domain.tarot.dto.RecTarotResponse;
import com.nightletter.domain.tarot.entity.PastTarot;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.repository.TarotRedisRepository;
import com.nightletter.domain.tarot.repository.TarotRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarotServiceImpl implements TarotService {

	private static final Map<Integer, TarotDto> deck = new HashMap<>();
	private final TarotRepository tarotRepository;
	private final WebClient webClient;
	private final TarotRedisRepository tarotRedisRepository;
	private final DiaryRepository diaryRepository;

	@PostConstruct
	private void getTarotEmbedded() {
		log.info("======== START MAKING DECK : Just wait 20sec. ==========");

		List<Tarot> allTarots = tarotRepository.findAll();
		List<TarotKeyword> allTarotsKeyword = new ArrayList<>();
		allTarots.forEach(tarot -> allTarotsKeyword.add(tarot.toKeywordDto()));

		RecTarotResponse tarotVectors = webClient.post()
			.uri("/tarots/init")
			.body(BodyInserters.fromValue(Map.of("tarots", allTarotsKeyword)))
			.retrieve()
			.bodyToMono(RecTarotResponse.class)
			.doOnError(error -> log.error("Fast API CONNECT ERROR: {}", error.getMessage()))
			.onErrorResume(error -> Mono.empty())
			.block();

		if (tarotVectors == null || tarotVectors.getTarots() == null) {
			log.error("Tarot vectors response is null or empty.");
			return;
		}

		tarotVectors.getTarots().forEach(tarotVec -> {
			Tarot tarot = allTarots.get(tarotVec.getId() - 1).setEmbedVector(tarotVec.getKeywords());
			deck.put(tarotVec.getId(), tarot.toDto());
		});
		log.info("======== COMPLETE MAKING DECK : {} ==========", deck.size());
	}

	@Override
	public TarotDto findSimilarTarot(EmbedVector diaryEmbedVector) {
		Map<Integer, Double> score = new HashMap<>();

		deck.forEach((tarotId, tarotDto) -> {
			List<Double> similarityVector = new ArrayList<>();

			tarotDto.embedVector().forEach(vector -> {
				double similarity = calculateCosineSimilarity(vector, diaryEmbedVector);
				similarityVector.add(similarity);
			});

			Double max = Collections.max(similarityVector);
			double sum = similarityVector.stream()
				.mapToDouble(Double::doubleValue)
				.sum();

			score.put(tarotId, max + sum);
		});
		log.info("score.size == {}", score.size());
		Integer key = score.entrySet()
			.stream()
			.max(Map.Entry.comparingByValue())
			.map(Map.Entry::getKey).get();

		log.info("{} no : this card == {}", key, deck.get(key).name());
		return deck.get(key);
	}

	@Override
	public TarotResponse findFutureTarot() {

		Diary diary = diaryRepository.findByWriterMemberIdAndDate(getCurrentMemberId(), LocalDate.now());
		DiaryTarot futureDiaryTarot = diary.getDiaryTarots()
			.stream()
			.filter(diaryTarot -> diaryTarot.getType() == DiaryTarotType.FUTURE)
			.findFirst()
			.get();

		futureDiaryTarot.getTarot().toDto();
		// todo. DTO 다시 반환
		return null;
	}

	private double calculateCosineSimilarity(EmbedVector embedVectorA, EmbedVector embedVectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		int size = embedVectorA.embed().size();
		for (int i = 0; i < size; i++) {
			dotProduct += embedVectorA.embed().get(i) * embedVectorB.embed().get(i);
			normA += Math.pow(embedVectorA.embed().get(i), 2);
			normB += Math.pow(embedVectorB.embed().get(i), 2);
		}

		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}


	@Override
	public Optional<TarotResponse> createRandomPastTarot() {
		// 과거 카드 redis 저장.
		// redisTemplate.
		int tarotId = new Random().nextInt(1, 157);

		Optional<Tarot> tarotResponse = tarotRepository.findById(tarotId);

		if (tarotResponse.isEmpty()) return Optional.empty();

		LocalDateTime expiredTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(4, 0));

		if (LocalTime.now().isAfter(LocalTime.of(4, 0))) {
			expiredTime = expiredTime.plusDays(1);
		}

		PastTarot pastTarot = tarotRedisRepository.save(PastTarot.builder()
			.memberId(getCurrentMemberId())
			.tarotId(tarotId)
			.expiredTime(expiredTime.toEpochSecond(ZoneOffset.UTC)
				- LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
			)
			.direction(tarotResponse.get().getDir())
			.build()
		);

		return tarotResponse.map(tarot -> TarotResponse.of(tarot, tarot.getDir()));
	}

	@Override
	public Optional<TarotResponse> getRandomPastTarot() {
		Optional<PastTarot> pastTarot = tarotRedisRepository.findById(getCurrentMemberId());

		return pastTarot.flatMap(
			value -> tarotRepository.findById(value.getTarotId()).map(tarot -> TarotResponse.of(tarot, tarot.getDir())));
	}


	private Integer getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Integer.parseInt((String) authentication.getPrincipal());
	}
}
