package io.getarrays.userservice.service.impl.user.profile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.domain.UserProfile;
import io.getarrays.userservice.repo.UserProfileRepo;
import io.getarrays.userservice.repo.UserRepo;
import io.getarrays.userservice.service.user.profile.AddProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AddProfileServiceImpl implements AddProfileService {
    private final UserRepo userRepo;

    private final UserProfileRepo profileRepo;

    @Override
    public Map<String, String> addProfile(HttpServletRequest request,Map <String, String> data) {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        User user = userRepo.findByUsername(username);
        String description = data.get("description");
        String title = data.get("title");
        String content = data.get("content");
        Map<String, String> map = new HashMap<>();
        if (title == null || title.length() == 0 || title.length() > 95) {
            map.put("error_message", "标题的长度不符合规范");
            return map;
        }
        if (description == null || description.length() == 0) {
            description = "这个用户很懒，什么也没留下~~";
        }
        if (description.length() > 500) {
            map.put("error_message", "描述过长，限制为500字符");
            return map;
        }
        Date date = new Date();
        UserProfile profile = new UserProfile(null, user.getId(), title, description, content, date, date);
        profileRepo.insert(profile);
        map.put("error_message", "success");
        return map;
    }
}
