package com.nightletter.domain.tarot.repository;

import java.util.List;

import com.nightletter.domain.tarot.dto.TarotDto;

public interface TarotCustomRepository {

	List<TarotDto> getAllTarots();
}
