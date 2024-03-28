package com.nightletter.domain.tarot.repository;

import java.util.List;

import com.nightletter.domain.tarot.dto.TarotKeyword;

public interface TarotCustomRepository {

	List<TarotKeyword> getAllTarots();
}
