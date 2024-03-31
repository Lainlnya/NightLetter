package com.nightletter.domain.tarot.entity;

import com.nightletter.domain.tarot.dto.TarotDto;

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
	private String description;
	@NotNull
	@Enumerated(EnumType.STRING)
	private TarotDirection dir;
	@Transient
	private String vector;

	protected Tarot() {
	}

	public Tarot(TarotDto tarotDto, String vector) {
		this.name = tarotDto.name();
		this.imgUrl = tarotDto.imgUrl();
		this.keyword = tarotDto.keyword();
		this.description = tarotDto.description();
		this.dir = tarotDto.dir();
		this.vector = vector;
	}
}
