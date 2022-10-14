package io.getarrays.userservice.service.user.profile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AddProfileService {
    Map<String, String> addProfile(HttpServletRequest request, Map<String, String> data);
}
