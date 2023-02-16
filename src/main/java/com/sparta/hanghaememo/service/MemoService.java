package com.sparta.hanghaememo.service;

import com.sparta.hanghaememo.dto.request.MemoRequestDto;
import com.sparta.hanghaememo.dto.response.MemoResponseDto;
import com.sparta.hanghaememo.dto.response.MessageResponseDto;
import com.sparta.hanghaememo.entity.Memo;
import com.sparta.hanghaememo.entity.User;
import com.sparta.hanghaememo.jwt.JwtUtil;
import com.sparta.hanghaememo.repository.MemoRepository;
import com.sparta.hanghaememo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 메모생성
    @Transactional
    public ResponseEntity<Object> createMemo(MemoRequestDto requestDto , HttpServletRequest request){
        // token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token 이 유효한지 검사
        if (token == null || !(jwtUtil.validateToken(token))) {
            throw new RuntimeException("토큰에러");
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        // token에서 가져온 사용자 정보를 사용하여 DB 조회
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) { // token 에서 가져온 사용자가 DB에 없는경우
            throw new RuntimeException();
        }

        // 작성글 저장
        Memo memo = memoRepository.save(Memo.builder()
                .requestDto(requestDto)
                .user(user.get())
                .build());

        // ResponseEntity로 반환
        return ResponseEntity.ok(new MemoResponseDto(memo));
    }

    // 전체 목록조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<MemoResponseDto>> getMemos() {
        List<Memo> memoList = memoRepository.findAllByOrderByModifiedAtDesc();
        List<MemoResponseDto> memoResponseDtosList = new ArrayList<>();

        // 댓글리스트 작성일자 기준 내림차순 정렬
        for (Memo memo : memoList) {
            MemoResponseDto tmp = new MemoResponseDto(memo);
            memoResponseDtosList.add(tmp);
        }
        return ResponseEntity.ok().body(memoResponseDtosList);
    }


    // 선택 메모 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getMemo(Long id) {
        // id 에 해당하는 게시글이 있는지 확인
        Optional<Memo> memo = memoRepository.findById(id);
        if (memo.isEmpty()) {
            throw new RuntimeException("게시글이 없습니다.");
        }

        // 해당 게시글이 있다면 게시글 객체를 Dto로 반환 후, ResponseEntity body 에 담아 리턴
        return ResponseEntity.ok(new MemoResponseDto(memo.get()));
    }

    // 선택 메모 수정
    @Transactional
    public ResponseEntity<Object> update(Long id, MemoRequestDto requestDto, HttpServletRequest request) {

        // request 에서 token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token 이 없거나 유효하지 않으면 게시글 수정 불가
        if (token == null || !(jwtUtil.validateToken(token))) {
            throw new RuntimeException("토큰에러");
        }

        claims = jwtUtil.getUserInfoFromToken(token);

        // token 에서 가져온 사용자 정보를 사용하여 DB 조회
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) { // token 에서 가져온 사용자가 DB에 없는경우
            throw new RuntimeException();
        }

        //선택한 게시글이 DB에 있는지 확인
        Optional<Memo> memo = memoRepository.findById(id);
        if (memo.isEmpty()) {
            throw new RuntimeException("게시글이 없습니다.");
        }

        // 게시글 id 와 사용자 정보 일치한다면, 게시글 수정
        memo.get().update(requestDto, user.get());
        memoRepository.saveAndFlush(memo.get());
        return ResponseEntity.ok(new MemoResponseDto(memo.get()));
    }

    //선택 메모 삭제
    @Transactional
    public ResponseEntity<Object> deleteMemo(Long id, HttpServletRequest request) {

        // request 에서 token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token 이 없거나 유효하지 않으면 게시글 삭제 불가
        if (token == null || !(jwtUtil.validateToken(token))) {
            throw new RuntimeException("토큰에러");
        }

        claims = jwtUtil.getUserInfoFromToken(token);

        // token 에서 가져온 사용자 정보를 사용하여 DB 조회
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) { // token 에서 가져온 사용자가 DB에 없는경우
            throw new RuntimeException();
        }

        //선택한 게시글이 DB에 있는지 확인
        Optional<Memo> memo = memoRepository.findById(id);
        if (memo.isEmpty()) {
            throw new RuntimeException("게시글이 없습니다.");
        }

        // 게시글 id 와 사용자 정보 일치한다면, 게시글 삭제
        memoRepository.deleteById(id);
        return ResponseEntity.ok(MessageResponseDto.builder()
                .statusCode(200)
                .msg("게시글 삭제 성공")
                .build());

    }
}
