package com.kof.matchingsystem.controller;

import com.kof.matchingsystem.service.MatchingService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Req
public class MatchingController {
    private final MatchingService matchingService;
}
