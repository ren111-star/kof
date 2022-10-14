package io.getarrays.userservice.service.impl.user.profile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.domain.UserProfile;
import io.getarrays.userservice.repo.UserProfileRepo;
import io.getarrays.userservice.repo.UserRepo;
import io.getarrays.userservice.service.user.profile.GetListProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetListProfileServiceImpl implements GetListProfileService {
    private final UserRepo userRepo;
    private final UserProfileRepo profileRepo;

    @Override
    public List<UserProfile> getProfileList(HttpServletRequest request) {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        User user = userRepo.findByUsername(username);
        QueryWrapper<UserProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        return profileRepo.selectList(queryWrapper);
    }
}
