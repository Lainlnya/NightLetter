package com.nightletter.domain.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightletter.domain.social.entity.Chat;
import com.nightletter.domain.social.entity.Chatroom;

public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
}
