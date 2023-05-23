package org.example.models.detectives;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetectivesItem{
    private String firstName;
    private String lastName;
    private Boolean violinPlayer;

    @JsonProperty("MainId")
    private Integer mainId;
    private List<CategoriesItem> categories;
}