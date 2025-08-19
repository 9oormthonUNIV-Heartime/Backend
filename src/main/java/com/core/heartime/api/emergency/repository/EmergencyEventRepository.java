package com.core.heartime.api.emergency.repository;

import com.core.heartime.api.emergency.entity.EmergencyEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyEventRepository extends JpaRepository<EmergencyEvent, Long> {
}
