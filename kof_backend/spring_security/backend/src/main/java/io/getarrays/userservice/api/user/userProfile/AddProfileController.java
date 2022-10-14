package io.getarrays.userservice.api.user.userProfile;

import io.getarrays.userservice.service.user.profile.AddProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AddProfileController {
    private final AddProfileService addProfileService;

    @PostMapping("/profile/add")
    public ResponseEntity<Map<String, String>> addProfile(HttpServletRequest request, @RequestParam Map<String, String> data) {

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/profile/add").toUriString());
        return ResponseEntity.created(uri).body(addProfileService.addProfile(request, data));
    }
}
