package com.poppin.userservice.entity;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "usernumber", nullable = false)
    private String usernumber;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "role", nullable = false)
    private String role;

}
