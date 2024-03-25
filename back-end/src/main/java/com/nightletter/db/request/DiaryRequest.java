package com.nightletter.db.request;

import com.nightletter.db.entity.Diary;
import com.nightletter.db.enums.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@ToString
public class DiaryRequest {
    private String content;
    private Type type;

    public Diary toEntity() {
        LocalDate today = LocalDate.now();

        if (LocalTime.now().isBefore(LocalTime.of(4, 0))) {
            today = today.minusDays(1);;
        }

        return Diary.builder()
                .date(today)
                .content(this.content)
                .type(this.type)
                .build();
    }
}
