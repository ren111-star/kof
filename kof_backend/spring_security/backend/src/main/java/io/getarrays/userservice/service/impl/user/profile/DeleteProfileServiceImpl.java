package io.getarrays.userservice.service.impl.user.profile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.domain.UserProfile;
import io.getarrays.userservice.repo.UserProfileRepo;
import io.getarrays.userservice.repo.UserRepo;
import io.getarrays.userservice.service.user.profile.DeleteProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeleteProfileServiceImpl implements DeleteProfileService {
    private final UserRepo userRepo;
    private final UserProfileRepo profileRepo;


    @Override
    public Map<String, String> removeProfile(HttpServletRequest request, Map<String, String> data) {
        int profile_id = Integer.parseInt(data.get("profile_id"));
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        User user = userRepo.findByUsername(username);
        UserProfile userProfile = profileRepo.selectById(profile_id);
        Map<String, String> map = new HashMap<>();
        if (null == userProfile) {
            map.put("error_message", "不存在该信息");
            return map;
        }
        if (!user.getId().equals(userProfile.getUserId())) {
            map.put("error_message", "非法删除");
            return map;
        }
        profileRepo.deleteById(profile_id);
        map.put("error_message", "success");
        return map;
    }
}
