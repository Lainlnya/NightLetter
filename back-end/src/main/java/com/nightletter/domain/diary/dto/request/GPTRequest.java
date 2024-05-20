package com.nightletter.domain.diary.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class GPTRequest implements Serializable {

    private String role;
    private String content;

    @Builder
    public GPTRequest(String role, String content) {
        this.role = role;
        this.content = content;

    }
}