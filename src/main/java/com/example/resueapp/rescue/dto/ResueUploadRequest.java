package com.example.resueapp.rescue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResueUploadRequest {
    @NotBlank(message = "Species is required (e.g., Dog, Cat).")
    @Size(max = 50, message = "Species name must not exceed 50 characters.")
    private String species;

    @NotBlank(message = "A short description of the animal's condition is required.")
    @Size(max = 500, message = "Description must not exceed 500 characters.")
    private String description;

    @NotBlank(message = "Street address details are required.")
    private String streetAddress;

    @NotNull(message = "An image file of the animal must be uploaded.")
    private MultipartFile file;

    @NotNull(message = "Latitude is missing")
    private Double latitude;

    @NotNull(message = "Longitude is missing")
    private Double longitude;
}
