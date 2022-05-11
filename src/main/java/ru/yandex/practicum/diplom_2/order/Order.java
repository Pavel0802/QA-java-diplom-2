package ru.yandex.practicum.diplom_2.order;

import io.qameta.allure.Step;
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

    @Step("Создание заказа неавторизированного пользователя")
    public Response createOrderUnauthorized() {
        return given()
                .spec(getBaseSpec())
                .body(json)
                .post(ORDER_PATH);

    }

    @Step("Создание заказа авторизированного пользователя")
    public Response createOrderAuthorized() {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .body(json)
                .post(ORDER_PATH);
    }

    @Step("Вывод заказов авторизированного пользователя")
    public Response getUserOrderAuthorized() {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .get(ORDER_PATH);
    }

    @Step("Вывод заказов неавторизированного пользователя")
    public Response getUserOrderUnauthorized() {
        return given()
                .spec(getBaseSpec())
                .get(ORDER_PATH);
    }
}
