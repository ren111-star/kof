package io.getarrays.userservice.api.pk;

import io.getarrays.userservice.service.pk.StartGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StartGameController {
    private final StartGameService startGameService;

    @PostMapping("/pk/start/game/")
    public String startGame (@RequestParam MultiValueMap<String, String> data) {
        String usernameA = data.getFirst("usernameA");
        String usernameB = data.getFirst("usernameB");
        return startGameService.startGame(usernameA, usernameB);
    }
}
