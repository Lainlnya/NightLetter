package com.nightletter.domain.diary.dto;

import java.util.List;

import com.nightletter.global.exception.CommonErrorCode;
import com.nightletter.global.exception.RecsysAnnoyTreeException;
import com.nightletter.global.exception.RecsysConnectionException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendDataResponse {

	private EmbedVector embedVector;
	private List<Long> diariesId;

	public void validation() {
		if (embedVector.embed().isEmpty()) {
			throw new RecsysConnectionException(CommonErrorCode.REC_SYS_CONNECTION_ERROR);
		} else if (diariesId.isEmpty()) {
			throw new RecsysAnnoyTreeException(CommonErrorCode.REC_ANNOY_TREE_ERROR);
		}
	}
}
