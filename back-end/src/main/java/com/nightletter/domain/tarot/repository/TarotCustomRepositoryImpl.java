package com.nightletter.domain.tarot.repository;

import static com.nightletter.domain.tarot.entity.QTarot.*;

import java.util.List;

import com.nightletter.domain.tarot.dto.TarotDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TarotCustomRepositoryImpl implements TarotCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<TarotDto> getAllTarots() {
		return queryFactory.select(Projections.constructor(TarotDto.class,
				tarot.id,
				tarot.name,
				tarot.imgUrl,
				tarot.keyword,
				tarot.description,
				tarot.dir
			))
			.from(tarot)
			.fetch();
	}
}
