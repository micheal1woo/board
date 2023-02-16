package com.sparta.hanghaememo.entity;


import com.sparta.hanghaememo.dto.request.MemoRequestDto;
import com.sparta.hanghaememo.dto.response.MemoResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Memo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn (name = "user_name",nullable = false)
    private User user;

    @Builder
    public Memo(MemoRequestDto requestDto, User user) {
        this.user = user;
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
    }

    public void update(MemoRequestDto requestDto, User user) {
        this.user = user;
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
    }
}