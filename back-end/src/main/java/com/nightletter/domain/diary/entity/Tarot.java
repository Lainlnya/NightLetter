package com.nightletter.domain.diary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Entity
public class Tarot {
    @Id
    private Integer tarotId;
    private String tarotName;
    private String forwardImgUrl;
    private String reverseImgUrl;
    private String forwardKeyword;
    private String reverseKeyword;
    private String tarotDesc;
}
