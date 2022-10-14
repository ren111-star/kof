package io.getarrays.userservice.service.user.profile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UpdateProfileService {
    Map<String, String> updateProfile(HttpServletRequest request, Map<String, String> data);
}
