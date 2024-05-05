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
import com.nightletter.domain.tarot.entity.TarotDirection;
import com.nightletter.domain.tarot.entity.TodayTarot;
import com.nightletter.domain.tarot.repository.TarotRedisRepository;
import com.nightletter.domain.tarot.repository.TarotRepository;
import com.nightletter.global.exception.CommonErrorCode;
import com.nightletter.global.exception.DupRequestException;
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

		return tarotRedisRepository.findById(getCurrentMemberId())
			.map(TodayTarot::getFutureCard)
			.orElseThrow(
				() -> new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "TODAY TAROT NOT FOUND"));
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

		// if (getPastTarot().isPresent()) {
		// 	throw new DupRequestException(CommonErrorCode.DUPLICATED_REQUEST_ERROR, "ALREADY POPPED");
		// }

		int tarotId = new Random().nextInt(156) + 1;
		TarotDirection direction = new Random().nextBoolean() ? TarotDirection.FORWARD : TarotDirection.REVERSE;

		Optional<Tarot> tarotResponse = tarotRepository.findById(tarotId);

		LocalDateTime expiredTime = LocalDateTime.of(getToday(), LocalTime.of(4, 0));

		tarotRedisRepository.save(
			TodayTarot.builder()
				.pastCard(tarotRepository.findById(tarotId)
					.map(tarot -> TarotResponse.of(tarot, direction))
					.orElseThrow(() ->
						new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "TAROT NOT FOUND")))
				.nowCard(null)
				.futureCard(null)
				.expiredTime(expiredTime.toEpochSecond(ZoneOffset.UTC)
					- LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
				.build()
		);

		return tarotResponse.map(tarot -> TarotResponse.of(tarot, tarot.getDir()));
	}

	@Override
	public Optional<TarotResponse> getPastTarot() {

		Integer memberId = getCurrentMemberId();

		// 캐시 조회.
		// 없으면 RDB 조회
		return Optional.ofNullable(
			tarotRedisRepository.findById(memberId)
			.map(TodayTarot::getPastCard)
			.orElse(
				tarotRepository.findPastTarot(getToday(), memberId)
					.map(tarot -> TarotResponse.of(tarot, tarot.getDir()))
					.orElseThrow(() ->
						new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "PAST TAROT NOT FOUND")))
		);

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
	public Optional<Tarot> findPastTarot() {
		/**
		 * RDB 이전에 Redis (오늘의 과거카드) 우선적으로 검색해야 함.
		 */
		Integer memberId = getCurrentMemberId();

		return tarotRedisRepository.findById(memberId)
			.map(TodayTarot::getPastCard)
			.map(pastTarot ->
			{
				return tarotRepository.findById(pastTarot.getId())
				.orElse(
					tarotRepository.findPastTarot(getToday(), memberId)
					.orElseThrow(() ->
						new ResourceNotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "TAROT NOT FOUND"))
				);
			}
		);

	}

	private Integer getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Integer.parseInt((String)authentication.getPrincipal());
	}

	private LocalDate getToday() {
		return LocalTime.now().isAfter(LocalTime.of(4, 0)) ?
				LocalDate.now() : LocalDate.now().minusDays(1);
	}
}
