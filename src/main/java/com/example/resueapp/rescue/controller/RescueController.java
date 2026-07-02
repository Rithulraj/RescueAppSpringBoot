package com.example.resueapp.rescue.controller;

import com.example.resueapp.rescue.dto.RescueDetailResponse;
import com.example.resueapp.rescue.dto.ResueUploadRequest;
import com.example.resueapp.rescue.service.RescueService;
import com.example.resueapp.rescue.service.RescueServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/rescue")
@RequiredArgsConstructor
@RestController
public class RescueController {

    private final RescueService rescueService;

    @PostMapping
    public ResponseEntity<RescueDetailResponse> uploadRescueDetail(
            @ModelAttribute @Valid ResueUploadRequest rescueUploadRequest
    ) {
        RescueDetailResponse res = rescueService.saveRescueDetail(rescueUploadRequest);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<RescueDetailResponse>> getAllRescue(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<RescueDetailResponse> allRescue = rescueService.getAllRescue(pageable);
        return ResponseEntity.ok(allRescue);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<RescueDetailResponse>> getAllRescueReportedByMe(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<RescueDetailResponse> allRescue = rescueService.getCasesReportedByMe(pageable);
        return ResponseEntity.ok(allRescue);
    }
}
