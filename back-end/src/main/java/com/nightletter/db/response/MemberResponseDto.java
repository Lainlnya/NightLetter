package com.nightletter.db.response;

import com.nightletter.db.entity.Member;
import com.nightletter.db.enums.Provider;
import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class MemberResponseDto {
    private Integer memberId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String OAuth2Id;

    private String email;

    private String nickname;

    private String profileImgUrl;

    private Provider provider;
}