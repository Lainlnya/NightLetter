package com.nightletter.domain.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightletter.domain.social.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository {
}
