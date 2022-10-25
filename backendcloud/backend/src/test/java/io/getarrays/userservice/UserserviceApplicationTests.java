package io.getarrays.userservice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.domain.UserInfo;
import io.getarrays.userservice.repo.UserInfoRepo;
import io.getarrays.userservice.repo.UserRepo;
import io.getarrays.userservice.service.user.account.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootTest
class UserserviceApplicationTests {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Test
    void contextLoads() {
        System.out.println("----------------------");
        userService.addRoleToUser("john", "ROLE_USER");
        System.out.println("-----------------------------");
    }

    @Test
    void SelectUserByUsernameTest() {
        String jwt = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwicm9sZXMiOlsiSm9obiBUcmF2b2x0YSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjY0MTE2NjYzfQ.UKUW2FsLqNcqv4AmbW16CIlLkuRA8qTRK64jC_7iJhg";
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        User user = userService.getUser(username);
        System.out.println("-----------------------------------------");
        System.out.println(user.getUsername());
        System.out.println(user.getName());
    }

    @Test
    void findUserByUsernameTest() {
        User will = userRepo.findByUsername("will");
        System.out.println(will);
    }

    @Test
    void addUser() {
        Map<String, String> user1 = new HashMap<>();
        user1.put("name", "Will Smith");
        user1.put("username", "will");
        user1.put("password", "1234");
        userService.saveUser(user1);
    }

    @Test
    void addRoleToUser() {
        userService.addRoleToUser("will", "ROLE_MANAGER");
        userService.addRoleToUser("jim", "ROLE_ADMIN");
        userService.addRoleToUser("arnold", "ROLE_SUPER_ADMIN");
        userService.addRoleToUser("arnold", "ROLE_ADMIN");
        userService.addRoleToUser("arnold", "ROLE_USER");
    }

    @Test
    void selectUserInfoTest() {
        String jwt = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcm5vbGQiLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9TVVBFUl9BRE1JTiIsIlJPTEVfVVNFUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjY0NjI0MzYxfQ.dAD-PyS48YEFidfing_MQZZ6wTL57IUY9QJVXPgLDds";
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        User user = userService.getUser(username);
        UserInfo userInfo = userInfoRepo.selectById(user.getId());
        String photo = userInfo.getPhoto();
        Map<String, String> data = new HashMap<>();
        data.put("id", user.getId().toString());
        data.put("username", username);
        data.put("photo", photo);
        System.out.println(data);
    }

    @Test
    void register () {
        Map<String, String> register = userService.register("rfx", "fuxiang", "1234", "1234");
        System.out.println(register);
    }

    @Test
    void createGameMap () {
        Random rand = new Random(); //instance of random class
        int upperbound = 25;
        //generate random values from 0-24
        int int_random = rand.nextInt(upperbound);
        String result = String.format("game_map %d created", int_random);
        System.out.println(result);
    }

    @Test
    void updateResult () {
        int yxc = userRepo.updateRatingByUsername(3, 1L);
        System.out.println(yxc > 0 ? "success" : "error");
    }
}
