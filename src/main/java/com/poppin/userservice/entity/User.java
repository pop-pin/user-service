package com.poppin.userservice.entity;

import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "usernumber", nullable = false)
    private String usernumber;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "email")
    private String email;
    @Column(name = "role")
    private String role;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public User(String username, String usernumber, String nickname, String email, String role) {
        this.username = username;
        this.usernumber = usernumber;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateUser(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }
}
