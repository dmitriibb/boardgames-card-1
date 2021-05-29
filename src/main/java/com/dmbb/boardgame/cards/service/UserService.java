package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    UserDTO register(UserDTO dto);

}
