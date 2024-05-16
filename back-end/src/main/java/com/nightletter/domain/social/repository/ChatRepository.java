package com.nightletter.domain.social.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nightletter.domain.social.dto.response.ChatResponse;
import com.nightletter.domain.social.entity.Chat;
import com.nightletter.domain.tarot.entity.Tarot;
import com.nightletter.domain.tarot.repository.TarotCustomRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer>, ChatCustomRepository {
}
