package ru.yandex.practicum.diplom_2.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest {
    private UserRequest userRequest;
    private User user;

    @Before
    public void setUp() {
        userRequest = new UserRequest();
        user = User.createUser();
        userRequest.create(user.toString());
    }

    @After
    public void tearDown() {
        JsonElement accessTokenFull = userRequest.login(UserLogin.from(user).toString()).thenReturn()
                .body().as(JsonObject.class).get("accessToken");
        String accessToken = accessTokenFull.toString().substring(8, 179);
        if (accessToken != null) {
            Tokens.setAccessToken(accessToken);
            userRequest.delete().then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Тест проверяет возможность авторизации пользователя и вывода в случае успеха accessToken пользователя")
    public void userCanCreatedAndBeLogIn() {
        userRequest.login(UserLogin.from(user).toString()).then().assertThat()
                .body("accessToken", notNullValue())
                .statusCode(200);
    }

    @Test
    @DisplayName("Авторизация пользователя без пароля")
    @Description("Тест проверяет появление ошибки в случае указания не всех обязательных полей, без поля password")
    public void userLoginWithoutRequiredFieldPassword() {
        UserLogin userLogin = new UserLogin(user.login, "");
        userRequest.login(userLogin.toString()).then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(401);
    }

    @Test
    @DisplayName("Авторизация пользователя без логина")
    @Description("Тест проверяет появление ошибки в случае указания не всех обязательных полей, без поля email")
    public void userLoginWithoutRequiredFieldEmail() {
        UserLogin userLogin = new UserLogin("", user.password);
        userRequest.login(userLogin.toString()).then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(401);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Тест проверяет появление ошибки в случае указания неверных регистрационных данных, в частности несуществующего email пользователя")
    public void courierIdIncorrectFieldEmail() {
        UserLogin userLogin = new UserLogin("111" + user.login, user.password);
        userRequest.login(userLogin.toString()).then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(401);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Тест проверяет появление ошибки в случае указания неверных регистрационных данных, в частности несуществующего password пользователя")
    public void courierIdIncorrectFieldPassword() {
        UserLogin userLogin = new UserLogin(user.login, user.password + "111");
        userRequest.login(userLogin.toString()).then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .statusCode(401);
    }
}
