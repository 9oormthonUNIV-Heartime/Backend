package com.core.heartime.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {

    /**
     * 200 OK
     */
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
    MEMBER_GET_SUCCESS(HttpStatus.OK, "회원 정보 조회 성공"),
    MEMBER_RESIGN_DELETE_SUCCESS(HttpStatus.OK, "회원탈퇴 성공"),
    MEMBER_INFO_GET_SUCCESS(HttpStatus.OK, "현재 사용자 정보 조회 성공"),
    ARTICLE_GET_SUCCESS(HttpStatus.OK,"게시글 조회 성공"),
    ARTICLE_UPDATE_SUCCESS(HttpStatus.OK,"게시글 수정 성공"),
    MEMBER_SIGNUP_SUCCESS(HttpStatus.OK,"회원가입 성공"),
    AUTH_SUCCESS(HttpStatus.OK, "인증에 성공했습니다."),
    PARTICIPATION_APPLY_SUCCESS(HttpStatus.OK, "봉사신청 성공"),
    PARTICIPATION_GET_SUCCESS(HttpStatus.OK, "봉사자 목록 조회 성공"),
    MY_PAGE_GET_SUCCESS(HttpStatus.OK, "마이페이지 정보 조회 성공"),
    MY_POST_LIST_GET_SUCCESS(HttpStatus.OK, "내 게시글 조회 성공"),
    CHATROOM_LIST_SUCCESS(HttpStatus.OK, "채팅방 목록 조회 성공"),
    CHAT_MESSAGES_SUCCESS(HttpStatus.OK, "채팅 메시지 조회 성공"),
    CHAT_MESSAGES_GET_SUCCESS(HttpStatus.OK, "채팅 메시지 목록 조회 성공"),
    IMAGE_UPLOAD_POST_SUCCESS(HttpStatus.OK, "이미지 업로드 성공"),
    IMAGE_DELETE_SUCCESS(HttpStatus.OK, "이미지 삭제 성공"),
    MY_VOLUNTEER_POST_LIST_GET_SUCCESS(HttpStatus.OK, "내 봉사 참여글(내역) 조회 성공"),
    RANK_GET_SUCCESS(HttpStatus.OK, "Top 10 랭킹 조회 성공"),
    MEMBER_UPDATE_SUCCESS(HttpStatus.OK, "회원정보 수정 성공"),
    CHAT_OPPONENT_NICKNAME_SUCCESS(HttpStatus.OK, "상대방 닉네임 조회 성공"),
    POST_ID_GET_SUCCESS(HttpStatus.OK, "PostId 조회 성공"),
    POST_AUTHENTICATE_SUCCESS(HttpStatus.OK, "봉사 요청글 인증 성공"),
    LOCATION_UPSERT_SUCCESS(HttpStatus.OK, "내 위치 업데이트 성공"),
    EMERGENCY_SIGNAL_HANDLED(HttpStatus.OK, "응급 신호 처리에 성공했습니다."),
    /**
     * 201 CREATED
     */
    ARTICLE_CREATE_SUCCESS(HttpStatus.CREATED, "게시판 등록 성공"),
    CHAT_MESSAGE_SENT(HttpStatus.CREATED, "채팅 메시지 전송 성공"),
    CHAT_ROOM_CREATE_SUCCESS(HttpStatus.CREATED, "채팅방 생성 성공"),
    VOLUNTEER_PARTICIPATE_SET_SUCCESS(HttpStatus.CREATED, "참여자 지정 성공"),

    /**
     * 204 NO CONTENT
     */
    ARTICLE_DELETE_SUCCESS(HttpStatus.NO_CONTENT,"게시글 삭제 성공"),


    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
