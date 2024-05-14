package com.nightletter.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 내부에서 작동하는 부분. /topic 구독하는 사람들에게 알림과 메세지를 줌.
		// config.enableSimpleBroker("/room");
		// 외부에서 사용하는 부분. 클라이언트가 /app 경로를 통해서 메세지를 발송함.
		// config.setApplicationDestinationPrefixes("/send");

		// 내부에서 작동하는 부분. /room 구독하는 사람들에게 알림과 메세지를 줌.
		config.enableSimpleBroker("/room");
		// 외부에서 사용하는 부분. 클라이언트가 /send 경로를 통해서 메세지를 발송함.
		config.setApplicationDestinationPrefixes("/send");
	}

	// TODO endPoint 동적으로 변경.
	// TODO Security 설정 필요.
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-stomp")
			.setAllowedOrigins("http://localhost:5500", "https://localhost:3001", "*");
	}

}