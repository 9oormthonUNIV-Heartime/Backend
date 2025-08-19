package com.core.heartime.api.notification.service;

import com.core.heartime.api.notification.dto.EmergencyAlertPayLoad;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationService {

    @Async
    public int sendEmergencyAlert(List<String> helperEmails, EmergencyAlertPayLoad payload) {
        log.info("[응급알림] 대상: {}, 제목: {}", helperEmails.size(), payload.getTitle());

        // 실제 알림 전송 로직은 Firebase, SMS, WebSocket 등으로 연동 가능
        for (String email : helperEmails) {
            log.info("알림 전송 대상: {}, 내용: {}", email, payload.getMessage());
            // 예: firebaseService.sendPushNotification(email, payload);
        }

        return helperEmails.size(); // 실제 성공 전송 수를 리턴할 수도 있음
    }
}
