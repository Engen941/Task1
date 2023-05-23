package org.example.models.detectives;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetectivesResponse {

    private Boolean success;

    private List<DetectivesItem> detectives;

    private String errorMessage;
}