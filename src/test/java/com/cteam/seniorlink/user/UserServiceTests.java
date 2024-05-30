package com.cteam.seniorlink.user;

import com.cteam.seniorlink.user.role.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("관리자 회원가입")
    public void 관리자_회원가입_테스트(){
        UserCreateRequest userRequest = new UserCreateRequest();
        userRequest.setUsername("admin");
        userRequest.setPassword1("111");
        userRequest.setPassword2("111");
        userRequest.setName("관리자");
        userRequest.setPhone("010-1234-5678");
        userRequest.setEmail("admin@email.com");
        userRequest.setAddress("서울시");
        userRequest.setAddressDetail("서초구");
        userRequest.setRole(UserRole.ADMIN);
        userRequest.setGrade(0);

        userService.saveUser(userRequest);
    }

    @Test
    @DisplayName("요양보호사 회원가입")
    public void 요양보호사_회원가입_테스트(){
        for(int i=1; i<6; i++){
            UserCreateRequest userRequest = new UserCreateRequest();
            userRequest.setUsername("careGiver" + i);
            userRequest.setPassword1("111");
            userRequest.setPassword2("111");
            userRequest.setName("요양보호사" + i);
            userRequest.setPhone("010-1234-5678");
            userRequest.setEmail("careGiver" + i + "@email.com");
            userRequest.setAddress("서울시");
            userRequest.setAddressDetail("서초구");
            userRequest.setRole(UserRole.CAREGIVER);
            userRequest.setGrade(0);

            userService.saveUser(userRequest);
        }
    }

    @Test
    @DisplayName("시니어 회원가입")
    public void 시니어_회원가입_테스트(){
        for(int i=1; i<6; i++){
            UserCreateRequest userRequest = new UserCreateRequest();
            userRequest.setUsername("careReceiver" + i);
            userRequest.setPassword1("111");
            userRequest.setPassword2("111");
            userRequest.setName("시니어" + i);
            userRequest.setPhone("010-1234-5678");
            userRequest.setEmail("careReceiver" + i + "@email.com");
            userRequest.setAddress("서울시");
            userRequest.setAddressDetail("서초구");
            userRequest.setRole(UserRole.CARERECEIVER);
            userRequest.setGrade(0);

            userService.saveUser(userRequest);
        }
    }
}
