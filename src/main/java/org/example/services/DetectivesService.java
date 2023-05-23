package org.example.services;

import org.example.models.detectives.CategoriesItem;
import org.example.models.detectives.DetectivesResponse;

public class DetectivesService {


    public DetectivesResponse postDetectives(DetectivesResponse detectivesRequest) {

        if (detectivesRequest.getDetectives().size() < 1 || detectivesRequest.getDetectives().size() > 3) {
            return DetectivesResponse.builder()
                    .errorMessage("Детективов не может быть меньше 1 или больше 3")
                    .build();
        }

        if (detectivesRequest.getDetectives().stream()
                .anyMatch(detective -> detective.getMainId()>10 || detective.getMainId()<0)) {
            return DetectivesResponse.builder()
                    .errorMessage("MainId может случайным образом меняться от 0 до 10")
                    .build();
        }

        if (detectivesRequest.getDetectives().stream()
                .flatMap(detective -> detective.getCategories().stream())
                .map(CategoriesItem::getCategoryID)
                .allMatch(categoryID -> categoryID < 1 || categoryID > 2)) {
            return DetectivesResponse.builder()
                    .errorMessage("CategoryID может принимать только значения 1 или 2")
                    .build();
        }

        if (detectivesRequest.getDetectives().stream()
                .flatMap(detective -> detective.getCategories().stream())
                .filter(category -> category.getCategoryID() != 2)
                .anyMatch(category -> category.getExtra() == null)) {
            return DetectivesResponse.builder()
                    .errorMessage("Элемент extra может принимать значение null только для CategoryID=2")
                    .build();
        }

        if (detectivesRequest.getDetectives().stream()
                .flatMap(detective -> detective.getCategories().stream())
                .filter(category -> category.getCategoryID() == 1)
                .anyMatch(category -> category.getExtra() == null || category.getExtra().getExtraArray().size() < 1)) {
            return DetectivesResponse.builder()
                    .errorMessage("Массив extraArray должен иметь минимум один элемент для CategoryID=1")
                    .build();
        }

        if (detectivesRequest.getDetectives().stream()
                .noneMatch(detective -> detective.getFirstName().equals("Sherlock"))) {
            return DetectivesResponse.builder()
                    .errorMessage("Поле success принимает значение true только если в массиве detectives есть элемент с firstName =\"Sherlock\"")
                    .build();
        }

        return detectivesRequest;
    }
}
