package ru.yandex.practicum.diplom_2.order;

import org.apache.commons.lang3.RandomUtils;
import ru.yandex.practicum.diplom_2.user.BaseData;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientList extends BaseData {

    private boolean success;
    private List<Data> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public IngredientList   getAllIngredients() {
        IngredientList ingredientList = given()
                .spec(getBaseSpec())
                .get(BASE_URL + "ingredients/")
                .body()
                .as(IngredientList.class);
        return ingredientList;
    }

    String pp = ",";
    String orderRequest = "\"ingredients\":[";

    public void ingredientRequestCreate() {
        IngredientList ingredientList = new IngredientList();
        ingredientList = ingredientList.getAllIngredients();
        int randomCountIngredient = RandomUtils.nextInt(1, 4);
        int ingredientCount = ingredientList.getData().size();

        for (int i = 1; i <= randomCountIngredient; i++) {
            int randomIngredient = RandomUtils.nextInt(0, ingredientCount);
            if (i < randomCountIngredient) {
                pp = ",";
            } else {
                pp = "";
            }
            String ingredientOrder = ingredientList.getData().get(randomIngredient).get_id();
            orderRequest = orderRequest + "\"" + ingredientOrder + "\"" + pp;
                    }
        orderRequest = orderRequest + "]";
        System.out.println(orderRequest);
    }
}
