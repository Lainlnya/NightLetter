package com.nightletter.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.nightletter.domain.member.repository.MemberRepository;
import com.nightletter.domain.social.handler.HttpHandshakeInterceptor;
import com.nightletter.global.security.handler.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 내부에서 작동하는 부분. /topic 구독하는 사람들에게 알림과 메세지를 줌.
		// config.enableSimpleBroker("/room");
		// 외부에서 사용하는 부분. 클라이언트가 /app 경로를 통해서 메세지를 발송함.
		// config.setApplicationDestinationPrefixes("/send");

		/*
		 * 내부에서 작동하는 부분.
		 * /room : 채팅방 접속 시.
		 * /notification : 초기 접속 시 notification 구독. Websocket 연결과 동시에.
		 */
		//  /room 구독하는 사람들에게 알림과 메세지를 줌.
		config.enableSimpleBroker("/room", "/notification");
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

	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		return false;
	}
}