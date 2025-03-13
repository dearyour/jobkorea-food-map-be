package com.food.map.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	MEMBER_01(HttpStatus.BAD_REQUEST, "멤버 정보가 없습니다."),
	MEMBER_02(HttpStatus.BAD_REQUEST, "이미 존재하는 멤버입니다."),
	ERROR2(HttpStatus.BAD_REQUEST, "error_2");

	private final HttpStatus httpStatus;
	private final String errMsg;
}
