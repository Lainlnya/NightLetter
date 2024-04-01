package com.nightletter.domain.tarot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;

import org.springframework.cglib.core.Local;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.diary.dto.EmbedVector;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.dto.PastTarotResponse;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.dto.TarotKeyword;
import com.nightletter.domain.tarot.dto.TarotListResponse;
import com.nightletter.domain.tarot.entity.PastTarot;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.entity.TarotDirection;
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

	@PostConstruct
	private void getTarotEmbedded() {
		log.info("======== START MAKING DECK : Just wait 20sec. ==========");

		List<Tarot> allTarots = tarotRepository.findAll();
		List<TarotKeyword> allTarotsKeyword = new ArrayList<>();
		allTarots.forEach(tarot -> allTarotsKeyword.add(tarot.toKeywordDto()));

		TarotListResponse tarotVectors = webClient.post()
			.uri("/tarot/init")
			.body(BodyInserters.fromValue(Map.of("tarots", allTarotsKeyword)))
			.retrieve()
			.bodyToMono(TarotListResponse.class)
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

	private double calculateCosineSimilarity(EmbedVector embedVectorA, EmbedVector embedVectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		int size = embedVectorA.getEmbed().size();
		for (int i = 0; i < size; i++) {
			dotProduct += embedVectorA.getEmbed().get(i) * embedVectorB.getEmbed().get(i);
			normA += Math.pow(embedVectorA.getEmbed().get(i), 2);
			normB += Math.pow(embedVectorB.getEmbed().get(i), 2);
		}

		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

	public int getRandomTarotId(int... ignoreTarotsId){
		Random random = new Random();
		List<Integer> ignoredIdsList = Arrays.stream(ignoreTarotsId).boxed().toList();


		//todo. 뒤집힌것도 ignore
		int id = 0;
		int pair = 0;
		boolean isIgnored;
		do {
			id = random.nextInt(156) + 1;
			pair = (id % 2 == 0) ? id - 1 : id + 1;

			isIgnored = ignoredIdsList.contains(id);
		} while (isIgnored);

		return id;
	}

	@Override
	public Optional<PastTarotResponse> createRandomPastTarot() {
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

		return tarotResponse.map(tarot -> PastTarotResponse.of(tarot, tarot.getDir()));
	}

	@Override
	public Optional<PastTarotResponse> getRandomPastTarot() {
		Optional<PastTarot> pastTarot = tarotRedisRepository.findById(getCurrentMemberId());

		return pastTarot.flatMap(
			value -> tarotRepository.findById(value.getTarotId()).map(tarot -> PastTarotResponse.of(tarot, tarot.getDir())));
	}

	private Integer getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Integer.parseInt((String) authentication.getPrincipal());
	}
}
