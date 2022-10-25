package io.getarrays.userservice.consumer;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.getarrays.userservice.consumer.utils.Game;
import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.repo.RecordRepo;
import io.getarrays.userservice.repo.UserInfoRepo;
import io.getarrays.userservice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {
    private final static String addPlayerURL = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerURL = "http://127.0.0.1:3001/player/remove/";
    public static final ConcurrentHashMap<Long, WebSocketServer> users = new ConcurrentHashMap<>();
    public static UserRepo userRepo;
    @Autowired
    public void setUserRepo (UserRepo userRepo) {
        WebSocketServer.userRepo = userRepo;
    }

    public static RecordRepo recordRepo;
    @Autowired
    public void setRecordRepo (RecordRepo recordRepo) {
        WebSocketServer.recordRepo = recordRepo;
    }

    private static UserInfoRepo userInfoRepo;
    @Autowired
    public void setUserInfoRepo (UserInfoRepo userInfoRepo) {
        WebSocketServer.userInfoRepo = userInfoRepo;
    }

    private static RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate (RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }
    private User user;
    private Session session = null;

    private Game game = null;

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        this.session = session;
        this.user = userRepo.findByUsername(username);
        if (this.user != null) {
            System.out.println("connected");
            users.put(user.getId(), this);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    private void move (JSONObject data) {
        Object[] directions = Arrays.stream(data.getJSONArray("direction").toArray()).toArray();
        Set<Character> receiveSet = new HashSet<>();
        for (Object d : directions) {
            String s = d.toString();
            receiveSet.add(s.charAt(0));
        }
        if (game.getPlayerA().getId() == data.getLongValue("id")) {
            game.setA_steps(receiveSet);
            game.setFlag_a(true);
        } else if (game.getPlayerB().getId() == data.getLongValue("id")) {
            game.setB_steps(receiveSet);
            game.setFlag_b(true);
        }
    }

    private void result (JSONObject data) {
        long id = data.getLongValue("loser_id");
        game.setStatus("result");
        System.out.println("失败者是" + id);
        if (game.getPlayerA().getId() == id) {
            game.setLoser("A");
        }else if (game.getPlayerB().getId() == id) {
            game.setLoser("B");
        }
    }

    private void overOfShort (JSONObject data) {
        Long id = data.getLongValue("id");
        if (game != null && game.getPlayerA().getId().equals(id)) {
            game.setExit_a(true);
        } else if (game != null && game.getPlayerB().getId().equals(id)) {
            game.setExit_b(true);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start_matching".equals(event)) {
            startMatching();
        } else if ("stop_matching".equals(event)){
            stopMatching();
        } else if ("move".equals(event)) {
            move(data);
        } else if ("result".equals(event)) {
            result(data);
        } else if ("if_exit".equals(event)) {
            overOfShort(data);
        }
    }

    public static void startGame (String usernameA, String usernameB) {
        User a = userRepo.findByUsername(usernameA);
        User b = userRepo.findByUsername(usernameB);
        Game game = new Game(a.getId(), b.getId());
        game.createMap();
        if (users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if (users.get(b.getId()) != null)
            users.get(b.getId()).game = game;

        game.start();
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_x", game.getPlayerA().getX());
        respGame.put("a_y", game.getPlayerA().getY());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_x", game.getPlayerB().getX());
        respGame.put("b_y", game.getPlayerB().getY());
        respGame.put("game_map", game.getG());

        JSONObject respA = new JSONObject();
        respA.put("event", "success_matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", userInfoRepo.selectById(b.getId()).getPhoto());
        respA.put("game", respGame);
        if (users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event", "success_matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", userInfoRepo.selectById(a.getId()).getPhoto());
        respB.put("game", respGame);
        if (users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    private void startMatching() {
        System.out.println("start matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        restTemplate.postForObject(addPlayerURL, data, String.class);
    }

    private void stopMatching() {
        System.out.println("stop matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayerURL, data, String.class);
    }

    public void sendMessage (String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
