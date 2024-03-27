package com.nightletter.domain.diary.entity;

import com.nightletter.global.common.BaseEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Tarot {
    @Id
    Integer tarotId;
    String name;
    String imgUrl;
    String forwardKeyword;
    String reverseKeyword;
}
