package com.dmbb.boardgame.cards.config;

import com.dmbb.boardgame.cards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    private static final String AUTH_HEADER = "Authorization";

    private final UserService userService;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) throws AuthenticationException {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        Object sessionId = message.getHeaders().get("simpSessionId");
        Principal principal = accessor.getUser();

        log.info("--message: " + accessor.getCommand() + " - " + sessionId + " - " + (principal == null ? " " : principal.getName()));

        if (accessor.getCommand() != StompCommand.CONNECT)
            return message;

        String auth = accessor.getFirstNativeHeader(AUTH_HEADER);
        UsernamePasswordAuthenticationToken user = userService.getUserForWebSocketAuth(auth);
        accessor.setUser(user);

        return message;
    }

}
