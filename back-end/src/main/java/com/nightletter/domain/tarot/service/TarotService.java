package com.nightletter.domain.tarot.service;

import java.util.Optional;

import com.nightletter.domain.tarot.dto.PastTarotResponse;
import com.nightletter.domain.tarot.dto.TarotDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarotServiceImpl {

	private static final Map<Integer, TarotDto> deck = new HashMap<>();
	private final TarotRepository tarotRepository;
	private final WebClient webClient;

	@PostConstruct
	private void getTarotEmbedded() {
		log.info("======== START MAKING DECK : Just wait 20sec. ==========");

		List<Tarot> allTarots = tarotRepository.findAll();
		List<TarotKeyword> allTarotsKeyword = new ArrayList<>();
		allTarots.forEach(tarot -> allTarotsKeyword.add(tarot.toKeywordDto()));

		TarotListResponse tarotVectors = webClient.post()
			.uri("/tarots/init")
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
}
