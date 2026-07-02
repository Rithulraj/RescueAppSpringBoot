package com.example.resueapp.rescue.service;

import com.example.resueapp.rescue.dto.RescueDetailResponse;
import com.example.resueapp.rescue.entity.CaseStatus;
import com.example.resueapp.rescue.entity.ResueDetail;
import com.example.resueapp.rescue.dto.ResueUploadRequest;
import com.example.resueapp.rescue.repository.RescueRepository;
import com.example.resueapp.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class RescueServiceImpl implements RescueService{

    private final RescueRepository rescueRepository;
    private final MyS3StorageService s3StorageService;

    @Transactional
    @Override
    public RescueDetailResponse saveRescueDetail(ResueUploadRequest rescueUploadRequest) {
        String s3Key = s3StorageService.uploadImage(rescueUploadRequest.getFile());
        Long userId = SecurityUtils.getCurrentUserId();
        ResueDetail detail = rescueRepository.save(mapToRescueDetailEntity(rescueUploadRequest, s3Key, userId));
       return mapToRescueDetailResponse(detail);
    }

    @Override
    public Page<RescueDetailResponse> getAllRescue(Pageable pageable) {
        Page<ResueDetail> allRescue = rescueRepository.findAll(pageable);
        return allRescue.map(this::mapToRescueDetailResponse);
    }

    @Override
    public Page<RescueDetailResponse> getCasesReportedByMe(Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<ResueDetail> reportedCases = rescueRepository.findByRescuerId(userId, pageable);
        return reportedCases.map(this::mapToRescueDetailResponse);
    }

    ResueDetail mapToRescueDetailEntity(ResueUploadRequest rescueUploadRequest, String s3Key, Long userId) {
        return ResueDetail.builder()
                .species(rescueUploadRequest.getSpecies())
                .rescuerId(userId)
                .description(rescueUploadRequest.getDescription())
                .latitude(rescueUploadRequest.getLatitude())
                .longitude(rescueUploadRequest.getLongitude())
                .status(CaseStatus.REPORTED)
                .streetAddress(rescueUploadRequest.getStreetAddress())
                .s3FileKey(s3Key)
                .build();
    }

    RescueDetailResponse mapToRescueDetailResponse(ResueDetail detail) {
        return RescueDetailResponse.builder()
                .id(detail.getId())
                .rescuerId(detail.getRescuerId())
                .species(detail.getSpecies())
                .description(detail.getDescription())
                .latitude(detail.getLatitude())
                .longitude(detail.getLongitude())
                .status(detail.getStatus())
                .reportedAt(detail.getReportedAt())
                .updatedAt(detail.getUpdatedAt())
                .streetAddress(detail.getStreetAddress())
                .image(s3StorageService.generateImageUrl(detail.getS3FileKey()))
                .build();
    }
}
