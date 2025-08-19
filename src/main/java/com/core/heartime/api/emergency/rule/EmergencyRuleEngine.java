package com.core.heartime.api.emergency.rule;

import org.springframework.stereotype.Component;


@Component // 이 클래스를 자동으로 객체로 만들어서 관리해줌
public class EmergencyRuleEngine {
    //규칙 엔진 : 응급상황인지 판단하는 역할

    /**
     * - situation 이 명시되면 그대로 사용
     * - 아니면 심박수 기반 간단 판정:
     *   heartRate == 0 -> CARDIAC_ARREST
     *   (추가 룰은 필요 시 확장)
     * - 아무 조건도 안맞으면 null (응급 아님)
     */
    public String decideType(Double heartRate, String situation) {
        if (situation != null && !situation.isBlank()) {
            return situation;
        }
        if (heartRate != null && heartRate <= 0.0) {
            return "CARDIAC_ARREST";
        }
        // TODO: 뇌졸중(FAST 징후) 같은 룰은 추후 신호 추가되면 확장
        return null;
    }
}
