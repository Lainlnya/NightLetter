package com.nightletter.domain.diary.entity;

import com.nightletter.global.common.BaseEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "tarotId"))
public class Tarot extends BaseEntity {
    String name;
    String imgUrl;
    String forwardKeyword;
    String reverseKeyword;
}
