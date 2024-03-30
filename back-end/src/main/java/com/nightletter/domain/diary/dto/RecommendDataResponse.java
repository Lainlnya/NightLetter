package com.nightletter.domain.diary.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendDataResponse {

	private List<Double> vector;
	private List<Long> diariesId;
}
