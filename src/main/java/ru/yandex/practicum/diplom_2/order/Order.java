package ru.yandex.practicum.diplom_2.order;

import io.restassured.response.Response;
import ru.yandex.practicum.diplom_2.user.BaseData;
import ru.yandex.practicum.diplom_2.user.Tokens;

import static io.restassured.RestAssured.given;


public class Order extends BaseData {

    public static final String ORDER_PATH = BASE_URL + "orders/";
    String json;
    IngredientList ingredientList = new IngredientList();

    public String ingredientCreate() {
        ingredientList.ingredientRequestCreate();
        json = "{" + ingredientList.orderRequest + "}";
        return json;
    }

    public Response createOrderUnauthorized() {
        System.out.println(json);
        return given()
                .spec(getBaseSpec())
                .body(json)
                .post(ORDER_PATH);

    }

    public Response createOrderAuthorized() {
        System.out.println(json);
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .body(json)
                .post(ORDER_PATH);
    }

    public Response getUserOrderAuthorized() {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .get(ORDER_PATH);
    }

    public Response getUserOrderUnauthorized() {
        return given()
                .spec(getBaseSpec())
                .get(ORDER_PATH);
    }
}
