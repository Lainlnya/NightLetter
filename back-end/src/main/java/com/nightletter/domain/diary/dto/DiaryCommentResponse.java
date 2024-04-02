package com.nightletter.domain.diary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Getter
@NoArgsConstructor
public class DiaryCommentResponse implements Serializable {

    private String id;
    private String object;
    private LocalDate created;
    private String model;
    private List<GPTQuestionResponse> choices;

    @Builder
    public DiaryCommentResponse(String id, String object,
                              LocalDate created, String model,
                              List<GPTQuestionResponse> choices) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choices = choices;
    }
}

