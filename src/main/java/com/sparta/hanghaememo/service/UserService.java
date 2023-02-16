package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.dto.request.LoginRequestDto;
import com.sparta.hanghaememo.dto.response.MessageResponseDto;
import com.sparta.hanghaememo.dto.request.SignupRequestDto;
import com.sparta.hanghaememo.entity.User;
import com.sparta.hanghaememo.entity.UserRoleEnum;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
//    private static final String ADMIN_TOKEN = "키";

    //회원가입 기능
    @Transactional
    public MessageResponseDto signup(SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        String username = signupRequestDto.getUsername();


        // 입력한 username, password 유효성 검사 통과 못한 경우
        if (bindingResult.hasErrors()) {
            return MessageResponseDto.builder()
                    .msg("유효성 검사실패")
                    .statusCode(400)
                    .build();   // @valid 에서 exception 발생 시, 해당 메시지를 출력한다.

        }

        //회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }



        //저장
        User user = new User(signupRequestDto);
        userRepository.save(user);


        return MessageResponseDto.builder()
                .msg("회원가입 성공")
                .statusCode(200)
                .build();

    }

    // 로그인
    @Transactional(readOnly = true)
    public MessageResponseDto login(LoginRequestDto loginRequestDto , HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // header 에 들어갈 JWT 세팅
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return MessageResponseDto.builder()
                .statusCode(200)
                .msg("로그인 성공")
                .build();
    }
}
