package io.getarrays.userservice.api.user.userProfile;

import io.getarrays.userservice.service.user.profile.UpdateProfileService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class UpdateProfileController {
    private final UpdateProfileService updateProfileService;

    @PostMapping("/profile/update")
    public ResponseEntity<Map<String, String>> updateUserProfile (HttpServletRequest request, @RequestParam Map<String, String> data) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/profile/update").toUriString());
        return ResponseEntity.created(uri).body(updateProfileService.updateProfile(request, data));
    }
}
