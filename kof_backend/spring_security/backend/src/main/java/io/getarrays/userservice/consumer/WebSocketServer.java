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

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {
    public static final ConcurrentHashMap<Long, WebSocketServer> users = new ConcurrentHashMap<>();
    private static final CopyOnWriteArraySet<User> matchingPool = new CopyOnWriteArraySet<>();
    private static UserRepo userRepo;
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
            matchingPool.remove(this.user);
        }
    }

    private void move (JSONObject data) {
        Object[] directions = Arrays.stream(data.getJSONArray("direction").toArray()).toArray();
        Set<Character> receiveSet = new HashSet<>();
        System.out.println("---------begin----------");
        for (Object d : directions) {
            String s = d.toString();
            receiveSet.add(s.charAt(0));
        }
        if (game.getPlayerA().getId() == data.getLongValue("id")) {
            System.out.println(receiveSet);
            game.setA_steps(receiveSet);
            game.setFlag_a(true);
            System.out.println("传送给了A");
            System.out.println(game.getPlayerA().getSteps());
        } else if (game.getPlayerB().getId() == data.getLongValue("id")) {
            game.setB_steps(receiveSet);
            game.setFlag_b(true);
            System.out.println("传送给了B");
            System.out.println(game.getPlayerB().getSteps());
        }
        System.out.println("-----------end----------");
    }

    private void result (JSONObject data) {
        long id = data.getLongValue("loser_id");
        game.setStatus("result");
        if (game.getPlayerA().getId() == id) {
            game.setLoser("A");
        }else if (game.getPlayerB().getId() == id) {
            game.setLoser("B");
        }
        System.out.println("debug for result loser");
        System.out.println("-------------begin----------------");
        System.out.println("loser id" + id);
        System.out.println("A's id" + game.getPlayerA().getId());
        System.out.println("B's id" + game.getPlayerB().getId());
        System.out.println("-------------end------------------");
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

    private void stopMatching() {
        System.out.println("stop matching");
        matchingPool.remove(this.user);
    }

    private void startMatching() {
        System.out.println("start matching");
        matchingPool.add(this.user);

        while (matchingPool.size() >= 2) {
            Iterator<User> it = matchingPool.iterator();
            User a = it.next();
            User b = it.next();
            matchingPool.remove(a);
            matchingPool.remove(b);

            Game game = new Game(a.getId(), b.getId());
            game.createMap();
            users.get(a.getId()).game = game;
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
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "success_matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", userInfoRepo.selectById(a.getId()).getPhoto());
            respB.put("game", respGame);
            users.get(b.getId()).sendMessage(respB.toJSONString());

            System.out.println(game.getG());
        }
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
