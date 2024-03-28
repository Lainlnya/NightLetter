package com.nightletter.domain.tarot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightletter.domain.tarot.entity.Tarot;

public interface TarotRepository extends JpaRepository<Tarot, Integer> {


}
