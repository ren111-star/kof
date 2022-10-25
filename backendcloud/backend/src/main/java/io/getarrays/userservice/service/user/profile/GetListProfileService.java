package io.getarrays.userservice.service.user.profile;

import io.getarrays.userservice.domain.UserProfile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GetListProfileService {
    List<UserProfile> getProfileList(HttpServletRequest request);
}
