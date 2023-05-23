import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.example.models.detectives.*;
import org.example.services.DetectivesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.example.helpers.SerializeHelper.extractAs;

public class AllTests {

    static final public String VALID_REQUEST = """
            {
              "detectives": [
                {
                  "MainId": 1,
                  "firstName": "Sherlock",
                  "lastName": "Homes",
                  "violinPlayer": true,
                  "categories": [
                    {
                      "CategoryID": 1,
                      "CategoryName": "extras1",
                      "extra": {
                        "extraArray": [
                          {
                            "violin": 1
                          },
                          {
                            "cap": 2
                          }
                        ]
                      }
                    }
                  ]
                },
                {
                  "MainId": 3,
                  "firstName": "James",
                  "lastName": "Watson",
                  "violinPlayer": false,
                  "categories": [
                    {
                      "CategoryID": 2,
                      "CategoryName": "extras2",
                      "extra": null
                    }
                  ]
                }
              ],
              "success": true
            }
            """;

    private final DetectivesService detectivesService = new DetectivesService();

    @Test
    @Tag("positive")
    @DisplayName("CategoryID принимает значения 1 или 2")
    public void checkField() {
        var detectivesRequest = extractAs(DetectivesResponse.class, VALID_REQUEST);
        var resp = detectivesService.postDetectives(detectivesRequest);

        Assertions.assertThat(resp.getDetectives().stream()
                        .flatMap(detective -> detective.getCategories().stream())
                        .map(CategoriesItem::getCategoryID)
                        .allMatch(categoryID -> categoryID.equals(1) || categoryID.equals(2)))
                .isTrue();
    }

    @Test
    @Tag("negative")
    @DisplayName("Если количество детективов больше 3 то выдается ошибка")
    public void amountOfDetectivesTest() {
        var detectivesRequest = extractAs(DetectivesResponse.class, VALID_REQUEST);
        //Добавляем в список детективов еще 2 так чтобы их стало 4
        detectivesRequest.getDetectives().addAll(List.of(buildValidDetective(), buildValidDetective()));
        var response = detectivesService.postDetectives(detectivesRequest);

        Assertions.assertThat(response.getErrorMessage())
                .isEqualTo("Детективов не может быть меньше 1 или больше 3");
    }

    @Test
    @Tag("negative")
    @DisplayName("MainId может случайным образом меняться от 0 до 10")
    public void MainIdWithOutNum() {
        var detectivesRequest = extractAs(DetectivesResponse.class, VALID_REQUEST);
        //Делаем mainID=11
        detectivesRequest.getDetectives().get(0).setMainId(11);
        var response = detectivesService.postDetectives(detectivesRequest);

        Assertions.assertThat(response.getErrorMessage())
                .isEqualTo("MainId может случайным образом меняться от 0 до 10");
    }


    @Test
    @Tag("negative")
    @DisplayName("Элемент extra может принимать значение null только для CategoryID=2")
    public void extraEqualsNull() {
        var detectivesRequest = extractAs(DetectivesResponse.class, VALID_REQUEST);
        //Делаем Extra=null для CategoryID=2, чтобы сработала ошибка
        detectivesRequest.getDetectives().get(0).getCategories().get(0).setExtra(null);
        var response = detectivesService.postDetectives(detectivesRequest);

        Assertions.assertThat(response.getErrorMessage())
                .isEqualTo("Элемент extra может принимать значение null только для CategoryID=2");
    }

    @Test
    @Tag("positive")
    @DisplayName("Элемент extraArray должен иметь минимум один элемент для CategoryID=1")
    public void extraArrayNotEmpty() {
        var detectivesRequest = extractAs(DetectivesResponse.class, VALID_REQUEST);
        var response = detectivesService.postDetectives(detectivesRequest);
        if (response.getDetectives().stream()
                .flatMap(detective -> detective.getCategories().stream())
                .map(CategoriesItem::getCategoryID)
                .allMatch(categoryID -> categoryID.equals(1)))
            Assertions.assertThat(response.getDetectives().stream()
                            .flatMap(detective -> detective.getCategories().stream())
                            .map(CategoriesItem::getExtra)
                            .allMatch(extra -> extra.getExtraArray().size() > 0))
                    .isTrue();
    }

    @Test
    @Tag("negative")
    @DisplayName("Поле success принимает значение true только если в массиве detectives есть элемент с firstName =\"Sherlock\"")
    public void checkSuccess() {
        var detectivesRequest = extractAs(DetectivesResponse.class, VALID_REQUEST);
        //Имзеняем firstName=null чтобы сработала ошибка
        detectivesRequest.getDetectives().get(0).setFirstName("John");
        var response = detectivesService.postDetectives(detectivesRequest);
        Assertions.assertThat(response.getErrorMessage())
                .isEqualTo("Поле success принимает значение true только если в массиве detectives есть элемент с firstName =\"Sherlock\"");
    }

    private static DetectivesItem buildValidDetective() {
        return DetectivesItem.builder()
                .mainId(1)
                .firstName("some")
                .lastName("some")
                .violinPlayer(true)
                .categories(List.of(CategoriesItem.builder()
                        .categoryID(9)
                        .categoryName("some")
                        .extra(Extra.builder()
                                .extraArray(List.of(ExtraArrayItem.builder()
                                        .cap(4)
                                        .violin(5)
                                        .build()))
                                .build())
                        .build()))
                .build();
    }
}
