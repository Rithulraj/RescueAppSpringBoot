package com.example.resueapp.rescue.service;

import com.example.resueapp.rescue.dto.RescueDetailResponse;
import com.example.resueapp.rescue.dto.ResueUploadRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RescueService {
   RescueDetailResponse saveRescueDetail(ResueUploadRequest rescueUploadRequest);

   Page<RescueDetailResponse> getAllRescue(Pageable pageable);

   Page<RescueDetailResponse> getCasesReportedByMe(Pageable pageable);
}