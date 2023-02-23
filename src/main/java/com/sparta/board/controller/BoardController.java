package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestsDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시글 전체 목록 조회
    @GetMapping("/api/posts")
    public ResponseEntity<List<BoardResponseDto>> getPosts() {
        return boardService.getPosts();
    }

    //게시글 작성
    @GetMapping("/api/post")
    public ResponseEntity<Object> createPost(@RequestBody BoardRequestsDto requestDto, HttpServletRequest request) {
        return boardService.createPost(requestDto, request);
    }

    //게시글 선택조회
    @GetMapping("/api/post/{id}")
    public ResponseEntity<Object> getPost(@PathVariable Long id) {
        return boardService.getPost(id);
    }

    //게시글 수정
    @PutMapping("/api/post/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable Long id, @RequestBody BoardRequestsDto requestDto, HttpServletRequest request) {
        return boardService.updatePost(id, requestDto, request);
    }

    //게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable Long id, @RequestBody BoardRequestsDto requestDto, HttpServletRequest request) {
        return boardService.deletePost(id, request);
    }
}

