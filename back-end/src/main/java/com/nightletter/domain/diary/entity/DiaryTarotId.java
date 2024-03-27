package com.nightletter.domain.diary.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Embeddable
public class DiaryTarotId implements Serializable {
    private Long diaryId;
    private Long tarotId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        DiaryTarotId other = (DiaryTarotId) obj;

        return this.diaryId.equals(other.diaryId) && this.tarotId.equals(other.tarotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.diaryId, this.tarotId);
    }
}
