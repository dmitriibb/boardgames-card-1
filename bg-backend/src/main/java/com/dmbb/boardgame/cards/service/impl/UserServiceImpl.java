package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.exception.BGNotAuthorizedException;
import com.dmbb.boardgame.cards.exception.TmpException;
import com.dmbb.boardgame.cards.model.dto.UserDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.UserRoles;
import com.dmbb.boardgame.cards.repository.UserRepository;
import com.dmbb.boardgame.cards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO register(UserDTO dto) {
        validateUserDTO(dto);

        User user = userRepository.getByEmail(dto.getEmail());
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
        return getByEmail(s);
    }

    @Override
    public UsernamePasswordAuthenticationToken getUserForWebSocketAuth(String base64AuthHeader) throws AuthenticationException {

        User user = getUserFromBaseAuth(base64AuthHeader);

        // null credentials, we do not pass the password along
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }

    @Override
    public User getUserFromBaseAuth(String base64AuthHeader) {
        if (StringUtils.isEmpty(base64AuthHeader))
            throw new BGNotAuthorizedException("Authorization header is empty");

        byte[] decodedBytes = Base64.getDecoder().decode(base64AuthHeader.split(" ")[1]);
        String decodedString = new String(decodedBytes);
        String[] usernameAndPassword = decodedString.split(":");

        if (usernameAndPassword.length != 2)
            throw new BGNotAuthorizedException("Missing username or password");

        String username = usernameAndPassword[0];
        String password =  usernameAndPassword[1];

        User user = getByEmail(username);

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new AuthenticationCredentialsNotFoundException("Wrong password");

        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return getByEmail(username);
    }

    private User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BGNotAuthorizedException("User is not found for email: " + email));
    }

}
