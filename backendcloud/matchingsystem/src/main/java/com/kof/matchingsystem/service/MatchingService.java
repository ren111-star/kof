package com.kof.matchingsystem.service;

public interface MatchingService {
    String addPlayer (Long userId, Integer rating);
    String removePlayer (Long userId);
}
