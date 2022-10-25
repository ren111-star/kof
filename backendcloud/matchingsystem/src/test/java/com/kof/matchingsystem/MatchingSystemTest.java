package com.kof.matchingsystem;

import com.kof.matchingsystem.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MatchingSystemTest {
    @Autowired
    private UserRepo userRepo;

    @Test
    void findIdByUsername () {
        String yxc = userRepo.findUsernameById(12L);
        System.out.println(yxc);
    }
}
