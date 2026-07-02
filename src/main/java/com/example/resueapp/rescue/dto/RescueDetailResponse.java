package com.example.resueapp.rescue.dto;

import com.example.resueapp.rescue.entity.CaseStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RescueDetailResponse {
    private Long id;
    private Long rescuerId;
    private String species;
    private String description;
    private String image;
    private Double latitude;
    private Double longitude;
    private CaseStatus status;
    private String streetAddress;
    private LocalDateTime reportedAt;
    private LocalDateTime updatedAt;
}
