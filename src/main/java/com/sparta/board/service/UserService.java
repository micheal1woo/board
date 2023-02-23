package com.sparta.board.service;




import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.entity.User;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    @Transactional(readOnly = true)
    public ResponseEntity<Object> login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        //사용자 확인
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return responseException("사용자가 없습니다.");
        }
        //비밀번호 확인
        if (!user.get().getPassword().equals(password)) {
            return responseException("비밀번호가 다릅니다.");
        }

        // header에 들어갈 JWT 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));

        return responseException("로그인 성공");
    }

    public ResponseEntity<Object> signup(SignupRequestDto requestDto, BindingResult bindingResult) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        //입력한 username, password 유효성 검사 통과 못 한 경우

        if (bindingResult.hasErrors()) {
            return responseException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return responseException("중복된 사용자가 존재합니다.");
        }

        // 입력한  username, password 로 user 객체 만들어 repository 에 저장
        userRepository.save(User.builder()
                .requestDto(requestDto)
                .build());
        return responseException("회원가입 성공");

    }
    private static ResponseEntity<Object> responseException(String message) {
        return ResponseEntity
                .badRequest()
                .body(MessageResponseDto.builder()
                        .msg(message)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());
    }
}