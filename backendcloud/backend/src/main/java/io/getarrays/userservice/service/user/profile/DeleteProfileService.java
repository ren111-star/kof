package io.getarrays.userservice.service.user.profile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface DeleteProfileService {
    Map<String, String> removeProfile(HttpServletRequest request, Map<String, String> data);
}
