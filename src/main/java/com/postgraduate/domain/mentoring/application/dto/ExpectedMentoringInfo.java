package com.postgraduate.domain.mentoring.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class ExpectedMentoringInfo {
    Long mentoringId;
    Long seniorId;
    String nickName;
    String postgradu;
    String field;
    String professor;
    String date;
    int term;
    String chatLink;
}

/**
 * 2. 진행예정 탭: <확정대기> 탭에 있던 멘토링 중, 대학원생 선배가 멘토링을 수락한 것이 <진행예정> 탭으로 이동.
 * 다음은 멘토링 리스트 각각에 들어가는 항목임.
 *
 * 1) 멘토링 정보 노출: 대학원생 선배 정보 - [대학원생 선배 닉네임], [대학원], [연구실분야], [교수님], [대학원생 선배가 선택한 멘토링 시간],  [멘토링 가능한 시간 옵션 - 1차 배포에서는 40분으로 고정] 텍스트 형태로 노출.
 * * 정보 노출 형태
 * 닉네임: 000 선배와 멘토링
 * 대학원 | 연구실분야 | 교수님
 * 대학원생 선배가 선택한 멘토링 시간: 0000년 00월 00일 00시
 *
 * 2) 대학원 선배 오픈채팅방 링크 + 복사 기능
 * 대학원 선배 오픈채팅방 링크가 텍스트 형태로 적혀 있고, 그 옆에 [복사] 버튼이 있음. [복사] 버튼을 누르면 오픈채팅방 링크가 복사됨.
 *
 * 3) [멘토링 진행 완료하기] 버튼
 * 해당 버튼을 누르면 <진행예정> 탭에 있던 멘토링이 <완료> 탭으로 이동. 해당 버튼은 사전에 예정된 멘토링 시간이 지나면 새롭게 생겨남.
 *
 * 4) 대학원생 선배 프로필 연결 링크
 */