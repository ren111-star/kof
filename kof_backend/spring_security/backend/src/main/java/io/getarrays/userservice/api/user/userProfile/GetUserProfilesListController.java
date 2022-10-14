package io.getarrays.userservice.api.user.userProfile;

import io.getarrays.userservice.domain.UserProfile;
import io.getarrays.userservice.service.user.profile.GetListProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class GetUserProfilesListController {
    private final GetListProfileService getListProfileService;

    @GetMapping("/profile/getlist")
    public ResponseEntity<List<UserProfile>> getAllUserProfileList (HttpServletRequest request) {
        return ResponseEntity.ok().body(getListProfileService.getProfileList(request));
    }
}
