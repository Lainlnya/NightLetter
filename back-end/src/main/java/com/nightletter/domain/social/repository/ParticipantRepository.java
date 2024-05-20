package com.nightletter.domain.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightletter.domain.social.entity.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
}
