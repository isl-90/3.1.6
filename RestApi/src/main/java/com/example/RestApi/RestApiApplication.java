package com.example.RestApi;

import com.example.RestApi.model.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class RestApiApplication {

    public static void main(String[] args) {
        String Url = "http://94.198.50.185:7081/api/users";

        // Создаем RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // 1. Получить список всех пользователей
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(Url, String.class);//выполняем HTTP GET запрос по указанному URL
        HttpHeaders headers = responseEntity.getHeaders();//получаем все заголовки ответа, включая Set-Cookie header.

        String sessionId = headers.getFirst(HttpHeaders.SET_COOKIE);
        //заголовок  используется для передачи идентификатора сессии между клиентом и сервером
        //Значение этого заголовка, которое содержит идентификатор сессии, сохраняется в переменной sessionId


        // 2. Сохранить пользователя с id = 3
        User userToAdd = new User(3L, "James", "Brown", (byte) 33);
        HttpHeaders headersWithSession = new HttpHeaders();
        headersWithSession.add(HttpHeaders.COOKIE, sessionId);
        HttpEntity<User> addUserRequest = new HttpEntity<>(userToAdd, headersWithSession);
        ResponseEntity<String> addUserResponse = restTemplate.exchange(Url, HttpMethod.POST, addUserRequest, String.class);

        // 3. Изменить пользователя с id = 3
        User userToUpdate = new User(3L, "Thomas", "Shelby", (byte) 50); // Новые данные пользователя
        HttpEntity<User> updateUserRequest = new HttpEntity<>(userToUpdate, headersWithSession);
        ResponseEntity<String> updateUserResponse = restTemplate.exchange(Url, HttpMethod.PUT, updateUserRequest, String.class);

        // 4. Удалить пользователя с id = 3
        String deleteUserUrl = Url + "/3";
        HttpEntity<Void> deleteUserRequest = new HttpEntity<>(headersWithSession);
        ResponseEntity<String> deleteUserResponse = restTemplate.exchange(deleteUserUrl, HttpMethod.DELETE, deleteUserRequest, String.class);

        // Получить итоговый код
        String finalCode = addUserResponse.getBody() + updateUserResponse.getBody() + deleteUserResponse.getBody();
        System.out.println("Итоговый код: " + finalCode);
        System.out.println(finalCode.length());
    }
}
