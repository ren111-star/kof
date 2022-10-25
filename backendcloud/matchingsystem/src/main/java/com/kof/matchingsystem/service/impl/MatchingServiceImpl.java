package com.kof.matchingsystem.service.impl;

import com.kof.matchingsystem.service.MatchingService;
import com.kof.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Long userId, Integer rating) {
        System.out.println("add player:" + userId + " " + rating);
        matchingPool.addPlayer(userId, rating);
        return "add player success";
    }

    @Override
    public String removePlayer(Long userId) {
        System.out.println("remove player:" + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
