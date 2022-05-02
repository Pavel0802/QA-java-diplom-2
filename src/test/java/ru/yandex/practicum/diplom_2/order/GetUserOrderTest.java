package ru.yandex.practicum.diplom_2.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.diplom_2.user.Tokens;
import ru.yandex.practicum.diplom_2.user.User;
import ru.yandex.practicum.diplom_2.user.UserRequest;

import static org.hamcrest.Matchers.*;

public class GetUserOrderTest {

    private UserRequest userRequest;
    private User user;
    private Order order;
    String accessToken;

    @Before //создаем случайного пользователя для возможности авторизированного запроса
    public void setUp() {
        order = new Order();
        userRequest = new UserRequest();
        user = User.createUser();
        String accessTokenFull = userRequest.create(user.toString()).then().extract().body().path("accessToken");
        accessToken = accessTokenFull.substring(7, 178);
        if (accessToken != null) {
            Tokens.setAccessToken(accessToken);
        }
    }

    @After //удаляем созданного пользователя
    public void tearDown() {
        if (accessToken != null) {
            userRequest.delete().then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Вывод списка заказов авторизованного пользователя")
    @Description("Тест проверяет, что запрос положительный, список заказов пользователя не пустой")
    public void getAuthorizedUserOrderTest(){
               order.getUserOrderAuthorized().then().assertThat()
                .body("success", equalTo(true))
                .body("orders", notNullValue())
                .statusCode(200);
                    }

    @Test
    @DisplayName("Вывод списка заказов неавторизованного пользователя")
    @Description("Тест проверяет вывод ошибки и сообщение о необходимости авторизации при запросе списка заказов неавторизованного пользователя")
    public void getUnauthorizedUserOrderTest(){
                order.getUserOrderUnauthorized().then().assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
    }
}
