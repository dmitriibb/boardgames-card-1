package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.model.dto.ClientMessageDTO;
import com.dmbb.boardgame.cards.model.dto.UserDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.service.MessageReceiverService;
import com.dmbb.boardgame.cards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageReceiverService messageReceiverService;


    @GetMapping("/login")
    public UserDTO login(@AuthenticationPrincipal User user) {
        return user.toDTO();
    }

    @PutMapping("/registration")
    private UserDTO registration(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PutMapping("/rest/message")
    public void messageFromUserREST(@AuthenticationPrincipal User user,
                                    @RequestBody ClientMessageDTO messageDTO,
                                    @RequestHeader(name = "gameId", required = false) Integer gameId) {
        messageReceiverService.messageFromUserREST(messageDTO, user, gameId);
    }


}
