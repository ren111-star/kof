package com.kof.matchingsystem.service.impl.utils;

import com.kof.matchingsystem.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
@Component
public class MatchingPool extends Thread {
    private static List<Player> players = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    private final static String startGameURL = "http://127.0.0.1:3000/pk/start/game/";
    private static RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate (RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    private static UserRepo userRepo;
    @Autowired
    public void setUserRepo (UserRepo userRepo) {
        MatchingPool.userRepo = userRepo;
    }

    public void addPlayer(Long userId, Integer rating) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Long userId) {
        lock.lock();
        try {
            System.out.println(players);
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId))
                    newPlayers.add(player);
            }
            players = newPlayers;
            System.out.println(players);
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private void sendResult (Player a, Player b) {
        System.out.println(a.getUserId());
        System.out.println(b.getUserId());
        String usernameA = userRepo.findUsernameById(a.getUserId());
        String usernameB = userRepo.findUsernameById(b.getUserId());
        System.out.println(usernameA);
        System.out.println(usernameB);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("usernameA", usernameA);
        data.add("usernameB", usernameB);
        restTemplate.postForObject(startGameURL, data, String.class);
    }

    private boolean checkMatched (Player a, Player b) {
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    private void matchPlayers() {
        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i ++ ) {
            if (used[i]) continue;
            for (int j = i + 1; j < players.size(); j ++ ) {
                if (used[j]) continue;
                Player a = players.get(i), b = players.get(j);
                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    sendResult(a, b);
                }
            }
        }

        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    matchPlayers();
                    increaseWaitingTime();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
