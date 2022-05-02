package ru.yandex.practicum.diplom_2.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UnauthorizedChangeDataUserTest {
    private UserRequest userRequest;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userRequest = new UserRequest();
        user = User.createUser();
        userRequest.create(user.toString());
        JsonElement accessTokenFull = userRequest.login(UserLogin.from(user).toString()).thenReturn()
                .body().as(JsonObject.class).get("accessToken");
        accessToken = accessTokenFull.toString().substring(8, 179);
        Tokens.setAccessToken(accessToken);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userRequest.delete().then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Изменение email пользователя")
    @Description("Тест проверяет возможность изменения email неавторизированного пользователя")
    public void userChangeEmail() {
        String changeData = "{\"" + "email" + "\":" + "\"" + RandomStringUtils.randomAlphabetic(3) + user.login + "\"}";
        userRequest.changeUnauthorized(changeData).then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
    }

    @Test
    @DisplayName("Изменение пароля пользователя")
    @Description("Тест проверяет возможность изменения пароля неавторизированного пользователя")
    public void userChangePassword() {
        String changeData = "{\"" + "password" + "\":" + "\"" + RandomStringUtils.randomAlphabetic(3) + user.password + "\"}";
        userRequest.changeUnauthorized(changeData).then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
    }

    @Test
    @DisplayName("Изменение имени пользователя")
    @Description("Тест проверяет возможность изменения имени неавторизированного пользователя")
    public void userChangeName() {
        String changeData = "{\"" + "name" + "\":" + "\"" + RandomStringUtils.randomAlphabetic(3) + user.name + "\"}";
        userRequest.changeUnauthorized(changeData).then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .statusCode(401);
    }
}
