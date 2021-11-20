package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.exception.TmpException;
import com.dmbb.boardgame.cards.model.dto.UserDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.UserRoles;
import com.dmbb.boardgame.cards.repository.UserRepository;
import com.dmbb.boardgame.cards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO register(UserDTO dto) {
        validateUserDTO(dto);

        User user = userRepository.findByEmail(dto.getEmail());
        if (user != null)
            throw new TmpException("This email is occupied");

        user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setActive(true);

        Set<UserRoles> roles = new HashSet<>();
        roles.add(UserRoles.USER);
        roles.add(UserRoles.ADMIN);
        user.setRoles(roles);

        user = userRepository.save(user);

        return user.toDTO();
    }

    private void validateUserDTO(UserDTO dto) {
        if (StringUtils.isEmpty(dto.getEmail()))
            throw new TmpException("Email is empty");

        if (StringUtils.isEmpty(dto.getPassword()))
            throw new TmpException("Password is empty");
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByEmail(s);
    }
}
