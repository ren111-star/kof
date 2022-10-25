package io.getarrays.userservice;

import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Main {
    @Autowired
    private UserRepo userRepo;

    public static void main(String[] args) {
        Main m = new Main();
        User yxc = m.userRepo.findByUsername("yxc");
        System.out.println();
    }
}
