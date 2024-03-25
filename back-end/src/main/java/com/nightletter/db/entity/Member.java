package com.nightletter.db.entity;

import com.nightletter.db.enums.Provider;
import com.nightletter.db.response.MemberResponseDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter @SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@AttributeOverride(name = "id", column =  @Column(name = "member_id"))
public class Member extends BaseEntity{

    private String OAuth2Id;

    private String email;

    private String nickname;

    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private Provider provider;

}
