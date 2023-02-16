package com.sparta.hanghaememo.controller;

import com.sparta.hanghaememo.dto.request.MemoRequestDto;
import com.sparta.hanghaememo.dto.response.MemoResponseDto;
import com.sparta.hanghaememo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemoController {
    private final MemoService memoService;
    @GetMapping("/")
    public ModelAndView home(){
        return new ModelAndView("index");
    }

    // 게시글 작성 API
    @PostMapping("/api/memos")
    public ResponseEntity<Object> createMemo(@RequestBody MemoRequestDto requestDto, HttpServletRequest request){

        return memoService.createMemo(requestDto, request);

    }
    // 전체 게시글 목록 조회 API
    @GetMapping("/api/memos")
    public ResponseEntity<List<MemoResponseDto>> getMemos(){
        return memoService.getMemos();

    }
    // 선택한 게시글 조회 API
    @GetMapping("/api/memos/{id}")
    public ResponseEntity<Object> getMemo(@PathVariable Long id){
        return memoService.getMemo(id);
    }


    // 선택한 게시글 수정 API
    @PostMapping("/api/memos/{id}")
    public ResponseEntity<Object> updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto, HttpServletRequest request){
        return memoService.update(id, requestDto, request);
    }
    // 선택 게시글 삭제 API
    @DeleteMapping("/api/memos/{id}")
    public ResponseEntity<Object> deleteMemo(@PathVariable Long id, HttpServletRequest request){
        return memoService.deleteMemo(id, request);
    }



}
