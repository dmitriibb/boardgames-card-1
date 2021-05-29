package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.model.dto.UserDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public UserDTO login(@AuthenticationPrincipal User user) {
        return user.toDTO();
    }

    @PutMapping("/registration")
    private UserDTO registration(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }


}
