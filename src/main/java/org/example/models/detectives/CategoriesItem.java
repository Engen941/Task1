package org.example.models.detectives;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesItem{

    @JsonProperty("CategoryID")
    private Integer categoryID;
    private Extra extra;

    @JsonProperty("CategoryName")
    private String categoryName;
}
