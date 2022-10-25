package io.getarrays.userservice.api.user.userProfile;

import io.getarrays.userservice.service.user.profile.DeleteProfileService;
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

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class RemoveProfileController {
    private final DeleteProfileService deleteProfileService;

    @PostMapping("/profile/remove")
    public ResponseEntity<Map<String, String>> removeUserProfile (HttpServletRequest request, @RequestParam Map<String, String> data) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/profile/remove").toUriString());
        return ResponseEntity.created(uri).body(deleteProfileService.removeProfile(request, data));
    }
}
