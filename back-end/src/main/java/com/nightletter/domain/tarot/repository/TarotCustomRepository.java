package com.nightletter.domain.tarot.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.nightletter.domain.tarot.entity.Tarot;

public interface TarotCustomRepository {

	Optional<Tarot> findPastTarot(LocalDate today, int memberId);

	Optional<Tarot> findNowTarot(LocalDate today, int memberId);
}
