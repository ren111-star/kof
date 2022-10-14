package io.getarrays.userservice.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import io.getarrays.userservice.consumer.WebSocketServer;
import io.getarrays.userservice.domain.Record;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private final ReentrantLock lock = new ReentrantLock();
    private String result = null;
    private final Player playerA, playerB;
    private boolean exit_a, exit_b;
    private int exit_count;

    public void setExit_a(boolean exit_a) {
        lock.lock();
        try {
            this.exit_a = exit_a;
        } finally {
            lock.unlock();
        }
    }

    public void setExit_b(boolean exit_b) {
        lock.lock();
        try {
            this.exit_b = exit_b;
        } finally {
            lock.unlock();
        }
    }

    private String status = "playing";

    private String loser = "";

    private Set<Character> a_steps = null;
    private Set<Character> b_steps = null;

    private boolean flag_a = false;
    private boolean flag_b = false;

    public void setLoser(String loser) {
        lock.lock();
        try {
            this.loser = loser;
        } finally {
            lock.unlock();
        }
    }

    public void setStatus(String status) {
        lock.lock();
        try {
            this.status = status;
        } finally {
            lock.unlock();
        }
    }

    public void setFlag_a(boolean flag_a) {
        lock.lock();
        try {
            this.flag_a = flag_a;
        } finally {
            lock.unlock();
        }
    }

    public void setFlag_b(boolean flag_b) {
        lock.lock();
        try {
            this.flag_b = flag_b;
        } finally {
            lock.unlock();
        }
    }

    public void setA_steps(Set<Character> stepsA) {
        lock.lock();
        try {
            this.a_steps = stepsA;
        } finally {
            lock.unlock();
        }
    }

    public void setB_steps(Set<Character> stepsB) {
        lock.lock();
        try {
            this.b_steps = stepsB;
        } finally {
            lock.unlock();
        }
    }


    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public Game(Long idA, Long idB) {
        playerA = new Player(idA, 30, 0, new HashSet<>());
        playerB = new Player(idB, 230, 0, new HashSet<>());
    }

    public void createMap() {
        Random rand = new Random(); //instance of random class
        int upperbound = 25;
        //generate random values from 0-24
        int int_random = rand.nextInt(upperbound);
        result = String.format("game_map %d created", int_random);
    }

    private void receiveSteps() {
        /*
            for (int i = 0; i < 5; i ++ ) {
                try {
                    Thread.sleep(1000);
                    lock.lock();
                    try {
                        if (a_steps != null && b_steps != null) {
                            return true;
                        }
                    } finally {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return false;
        **/
        lock.lock();
        try {
            playerA.setSteps(a_steps);
        } finally {
            lock.unlock();
        }
        lock.lock();
        try {
            playerB.setSteps(b_steps);
        } finally {
            lock.unlock();
        }

    }

    private int judge_end() {
        return 0;
    }

    public String getG() {
        return result;
    }

    private void saveToDatabase () {
        Record record = new Record(
                null,
                playerA.getId(),
                playerB.getId(),
                loser,
                new Date()
        );
        WebSocketServer.recordRepo.insert(record);
    }

    private void sendResult() {
        saveToDatabase();
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        sendAllMessage(resp.toJSONString());
    }

    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null) WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null) WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void judge() {

    }

    private void sendMove(Long fromId, Long toId, Set<Character> opponentOperate) {
        JSONObject resp = new JSONObject();
        resp.put("event", "move");
        resp.put("opponent_id", fromId);
        resp.put("opponent_operate", opponentOperate);
        WebSocketServer.users.get(toId).sendMessage(resp.toJSONString());
    }

    private void askExit() {
        JSONObject resp = new JSONObject();
        resp.put("event", "if_exit");
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                status = "timeout";
            }
        };
        TimerTask ifExit = new TimerTask() {
            @Override
            public void run() {
                askExit();

                if (exit_count >= 3) {
                    lock.lock();
                    try {
                        if (!exit_a || !exit_b) {
                            if (!exit_a && !exit_b) {
                                loser = "all";
                            } else if (!exit_a) {
                                loser = "B";
                            } else {
                                loser = "A";
                            }
                            setStatus("result");
                        }

                    } finally {
                        lock.unlock();
                    }
                }
                if (++exit_count % 3 == 0) {
                    setExit_a(false);
                    setExit_b(false);
                }

                if (!"playing".equals(status)) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 60 * 1000);
        timer.schedule(ifExit, 2000, 2000);
        label:
        while (true) {
            switch (status) {
                case "playing":
                    lock.lock();
                    try {
                        if (flag_a) {
                            sendMove(playerA.getId(), playerB.getId(), a_steps);
                            flag_a = false;
                        }
                        if (flag_b) {
                            sendMove(playerB.getId(), playerA.getId(), b_steps);
                            flag_b = false;
                        }
                    } finally {
                        lock.unlock();
                    }
                    judge();
                    break;
                case "result":
                    sendResult();
                    break label;
                case "timeout":
                    if (judge_end() > 0) {
                        loser = "B";
                    } else if (judge_end() < 0) {
                        loser = "A";
                    } else if (judge_end() == 0) {
                        loser = "all";
                    }
                    sendResult();
                    break label;
            }
        }
    }
}
