package com.nightletter.domain.tarot.entity;

import java.util.List;

import com.nightletter.domain.tarot.dto.TarotDto;
import com.nightletter.domain.tarot.dto.TarotKeyword;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class Tarot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tarot_id")
	private Integer id;
	@NotNull
	private String name;
	@NotNull
	private String imgUrl;
	@NotNull
	private String keyword;
	@NotNull
	@Column(length = 1500)
	private String description;
	@NotNull
	@Enumerated(EnumType.STRING)
	private TarotDirection dir;
	@Transient
	private List<List<Double>> vector;

	protected Tarot() {
	}

	public TarotKeyword toKeywordDto(){
		return new TarotKeyword(id, keyword);
	}

	public TarotDto toDto(){
		return new TarotDto(id, name, imgUrl, keyword, description, dir, vector);
	}

	public Tarot setVector(List<List<Double>> vector) {
		this.vector = vector;
		return this;
	}
}
