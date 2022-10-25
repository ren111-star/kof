package io.getarrays.userservice.service.impl.pk;

import io.getarrays.userservice.consumer.WebSocketServer;
import io.getarrays.userservice.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {

    @Override
    public String startGame(String usernameA, String usernameB) {
        System.out.println("start game :\n usernameA:" + usernameA + " ,\n usernameB:" + usernameB);
        WebSocketServer.startGame(usernameA, usernameB);
        return "start game success";
    }
}
