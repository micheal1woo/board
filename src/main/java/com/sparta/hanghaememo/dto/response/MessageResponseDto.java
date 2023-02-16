package com.sparta.hanghaememo.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class MessageResponseDto {
    private String msg;
    private int statusCode;

    @Builder
    public MessageResponseDto(String msg, int statusCode){

        this.msg = msg;
        this.statusCode = statusCode;
    }

}
