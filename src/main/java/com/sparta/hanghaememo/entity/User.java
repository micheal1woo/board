package com.sparta.hanghaememo.entity;

import com.sparta.hanghaememo.dto.request.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public User(SignupRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        if (requestDto.isAdmin()) {
            this.role = UserRoleEnum.ADMIN;
        } else {  // admin = false 로 입력했을 경우
            this.role = UserRoleEnum.USER;
        }
    }

}