package com.dmbb.boardgame.cards.model.entity;

import com.dmbb.boardgame.cards.model.enums.UserRoles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@Getter
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    public UserRole(User user, UserRoles role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }
}
