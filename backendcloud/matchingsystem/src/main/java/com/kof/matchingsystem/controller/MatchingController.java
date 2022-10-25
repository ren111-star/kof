package com.kof.matchingsystem.controller;

import com.kof.matchingsystem.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    @PostMapping("/player/add/")
    public String addPlayer (@RequestParam MultiValueMap<String, String> data) {
        Long userId = Long.parseLong(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        return matchingService.addPlayer(userId, rating);
    }

    @PostMapping("/player/remove/")
    public String removePlayer (@RequestParam MultiValueMap<String, String> data) {
        Long userId = Long.parseLong(Objects.requireNonNull(data.getFirst("user_id")));
        System.out.println("remove user" + userId);
        return matchingService.removePlayer(userId);
    }
}
