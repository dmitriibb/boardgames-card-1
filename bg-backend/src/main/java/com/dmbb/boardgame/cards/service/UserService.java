package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.UserDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    UserDTO register(UserDTO dto);

    UsernamePasswordAuthenticationToken getUserForWebSocketAuth(String base64AuthHeader);

    User getUserFromBaseAuth(String base64AuthHeader);

    User getUserByUsername(String username);

}
