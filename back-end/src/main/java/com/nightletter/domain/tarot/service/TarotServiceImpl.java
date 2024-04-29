package com.nightletter.domain.tarot.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.nightletter.domain.diary.dto.recommend.EmbedVector;
import com.nightletter.domain.diary.entity.Diary;
import com.nightletter.domain.diary.entity.DiaryTarot;
import com.nightletter.domain.diary.entity.DiaryTarotType;
import com.nightletter.domain.diary.repository.DiaryRepository;
import com.nightletter.domain.member.entity.Member;
import com.nightletter.domain.tarot.dto.RecTarotResponse;
import com.nightletter.domain.tarot.dto.RecVectorResponse;
import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.dto.TarotKeyword;
import com.nightletter.domain.tarot.dto.TarotResponse;
import com.nightletter.domain.tarot.entity.PastTarot;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.repository.TarotRedisRepository;
import com.nightletter.domain.tarot.repository.TarotRepository;
import com.nightletter.global.exception.CommonErrorCode;
import com.nightletter.global.exception.RecsysConnectionException;
import com.nightletter.global.exception.ResourceNotFoundException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarotServiceImpl implements TarotService {

	private static final Map<Integer, TarotDto> deck = new ConcurrentHashMap<>();
	private final TarotRepository tarotRepository;
	private final WebClient webClient;
	private final TarotRedisRepository tarotRedisRepository;
	private final DiaryRepository diaryRepository;

	@PostConstruct
	public void getTarotEmbedded() {
		log.info("======== START MAKING DECK : Just wait. ==========");
		Flux.fromIterable(tarotRepository.findAll())
			.map(Tarot::toKeywordDto)
			.collectList()
			.flatMap(this::fetchTarotVectors)
			.flatMapMany(recTarotResponse -> Flux.fromIterable(recTarotResponse.getTarots()))
			.flatMap(this::updateTarotWithVector)
			.doOnNext(tarotDto -> deck.put(tarotDto.id(), tarotDto))
			.thenMany(Flux.fromIterable(deck.values()))
			.collectList()
			.doOnSuccess(tarotDtos -> log.info("======== COMPLETE MAKING DECK : {} ==========", tarotDtos.size()))
			.onErrorComplete()
			.subscribe();
	}

	private Mono<RecTarotResponse> fetchTarotVectors(List<TarotKeyword> keywords) {
		return webClient.post()
			.uri("/tarots/init")
			.body(BodyInserters.fromValue(Map.of("tarots", keywords)))
			.retrieve()
			.bodyToMono(RecTarotResponse.class)
			.onErrorResume(e ->
				Mono.error(new RecsysConnectionException(CommonErrorCode.REC_SYS_CONNECTION_ERROR)));
	}

	private Mono<TarotDto> updateTarotWithVector(RecVectorResponse tarotVector) {
		return Mono.justOrEmpty(tarotRepository.findAll()
				.stream()
				.filter(tarot -> tarot.getId().equals(tarotVector.getId()))
				.findFirst()
				.map(tarot -> tarot.setEmbedVector(tarotVector.getKeywords()).toDto()))
			.onErrorResume(e -> {
				return Mono.error(
					new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "TAROT NOT FOUND")); // 에러 시 대체 동작
			});
	}

	@Override
	public Tarot findSimilarTarot(EmbedVector diaryEmbedVector) {
		Map<Integer, Double> score = new ConcurrentHashMap<>();

		deck.entrySet().parallelStream().forEach(entry -> {
			double maxSimilarity = 0.0;
			double sumSimilarity = 0.0;

			for (EmbedVector vector : entry.getValue().embedVector()) {
				double similarity = calculateCosineSimilarity(vector, diaryEmbedVector);
				sumSimilarity += similarity;
				if (similarity > maxSimilarity) {
					maxSimilarity = similarity;
				}
			}
			score.put(entry.getKey(), maxSimilarity + sumSimilarity);
		});
		log.info("======== Find Similar Tarots.score.size : {} ========", score.size());

		int key = score.entrySet().stream()
			.max(Map.Entry.comparingByValue())
			.orElseThrow(() -> new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "TAROT KEY NOT FOUND"))
			.getKey();
		log.info("======== Similar Tarots : {} , no : {} ======== ", deck.get(key).name(), key);
		return tarotRepository.findById(key)
			.orElseThrow(
				() -> new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "NOW TAROT NOT FOUND"));
	}

	@Override
	public TarotResponse findFutureTarot() {
		// Diary diary = diaryRepository.findByWriterMemberIdAndDate(getCurrentMemberId(), LocalDate.now())
		// 	.orElseThrow(() -> new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND,
		// 			"DIARY NOT FOUND - MEMBER ID : " + getCurrentMemberId()));

		// TODO 오늘 날짜 수정 (오전 4시 기준)
		List<Diary> diaries = diaryRepository.findAllByWriterMemberIdAndDate(getCurrentMemberId(), LocalDate.now());
		// TODO INDEX ERROR 수정
		Diary diary = diaries.get(0);

		DiaryTarot futureDiaryTarot = diary.getDiaryTarots()
			.stream()
			.filter(diaryTarot -> diaryTarot.getType() == DiaryTarotType.FUTURE)
			.findFirst()
			.orElseThrow(
				() -> new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "DIARY-TAROT NOT FOUND"));

		return futureDiaryTarot.getTarot().toResponse();
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

		if (tarotResponse.isEmpty())
			return Optional.empty();

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
			value -> tarotRepository.findById(value.getTarotId())
				.map(tarot -> TarotResponse.of(tarot, tarot.getDir())));
	}

	private Integer getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Integer.parseInt((String)authentication.getPrincipal());
	}

	@Override
	public Tarot makeRandomTarot(int... ignoreTarotsId) {
		Random random = new Random();
		List<Integer> ignoredIdsList = Arrays.stream(ignoreTarotsId).boxed().toList();

		int id = 0;
		int pair = 0;
		boolean isIgnored;
		do {
			id = random.nextInt(156) + 1;
			pair = (id % 2 == 0) ? id - 1 : id + 1;

			isIgnored = ignoredIdsList.contains(id) || ignoredIdsList.contains(pair);
		} while (isIgnored);

		return tarotRepository.findById(id).orElseThrow(() ->
			new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "TAROT NOT FOUND"));
	}

	@Override
	public Tarot findPastTarot(Member currentMember) {
		return tarotRepository.findPastTarot(LocalDate.now(), currentMember.getMemberId())
			.orElseGet(() -> {
				PastTarot pastTarot = tarotRedisRepository.findById(getCurrentMemberId())
					.orElseThrow(() -> new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND,
						"PAST TAROT IN REDIS NOT FOUND"));

				return tarotRepository.findById(pastTarot.getTarotId())
					.orElseThrow(
						() -> new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "TAROT NOT FOUND"));
			});
	}
}
