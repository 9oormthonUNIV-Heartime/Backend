package com.core.heartime.common.advice;

import com.core.heartime.common.exception.BaseException;
import com.core.heartime.common.response.ApiResponse;
import com.core.heartime.common.response.ErrorStatus;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 처리 (ex. BAD_REQUEST, CONFLICT 등)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse> handleCostumeException(BaseException ex) {

        return ResponseEntity.status(ex.getStatusCode())
                .body(ApiResponse.fail(ex.getStatusCode(), ex.getMessage()));
    }

    // 글로벌 예외 처리 (ex. 명시되어 있지 않은 예외 발생)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex) {

        log.error("[handleException] 알 수 없는 오류 발생: ", ex);

        return ResponseEntity.status(ErrorStatus.INTERNAL_SERVER_ERROR.getStatusCode())
                .body(ApiResponse.fail(ErrorStatus.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "알 수 없는 오류 발생"));
    }

    // 잘못된 인자 전달 시 발생 (ex. 숫자 필드에 문자열 입력)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        return ResponseEntity.status(ErrorStatus.BAD_REQUEST_MISSING_PARAM.getStatusCode())
                .body(ApiResponse.fail(ErrorStatus.BAD_REQUEST_MISSING_PARAM.getStatusCode(), ex.getMessage()));
    }

    // 필수 요청 파라미터 누락 (ex. ID 없이 게시글 단건 조회)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParameterException(MissingServletRequestParameterException ex) {

        return ResponseEntity.status(ErrorStatus.BAD_REQUEST_MISSING_REQUIRED_FIELD.getStatusCode())
                .body(ApiResponse.fail(ErrorStatus.BAD_REQUEST_MISSING_REQUIRED_FIELD.getStatusCode(),
                        ErrorStatus.BAD_REQUEST_MISSING_REQUIRED_FIELD.getMessage() + ": " + ex.getParameterName()));
    }

    // DTO 유효성 검증 실패 (ex. @NotBlack 필드를 비운 채로 요청)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {

        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(),
                        error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(ErrorStatus.BAD_REQUEST_MISSING_PARAM.getStatusCode())
                .body(ApiResponse.fail(ErrorStatus.BAD_REQUEST_MISSING_PARAM.getStatusCode(), errorMsg));
    }

    // DB에 존재하지 않는 리소스 접근/조작 시도 (ex. 없는 ID의 아티클 접근)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResponse> handleEmptyResultDataAccessException(final EmptyResultDataAccessException ex) {

        return ResponseEntity.status(ErrorStatus.NOT_FOUND_RESOURCE.getStatusCode())
                .body(ApiResponse.fail(ErrorStatus.NOT_FOUND_RESOURCE.getStatusCode(),
                        ErrorStatus.NOT_FOUND_RESOURCE.getMessage()));
    }
}
