package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.request.LoginRequestDto;
import com.sparta.hanghaememo.dto.response.MessageResponseDto;
import com.sparta.hanghaememo.dto.request.SignupRequestDto;
import com.sparta.hanghaememo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public MessageResponseDto signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult){
        return userService.signup(signupRequestDto, bindingResult);
    }

    // 로그인
    @PostMapping("/login")
    public MessageResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return userService.login(loginRequestDto, response);
    }



}
