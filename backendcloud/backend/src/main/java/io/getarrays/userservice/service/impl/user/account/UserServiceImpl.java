package io.getarrays.userservice.service.impl.user.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.getarrays.userservice.domain.Role;
import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.domain.UserInfo;
import io.getarrays.userservice.repo.RoleRepo;
import io.getarrays.userservice.repo.UserInfoRepo;
import io.getarrays.userservice.repo.UserRepo;
import io.getarrays.userservice.service.user.account.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserInfoRepo userInfoRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database {}", username);
            throw new UsernameNotFoundException("User not fount in the database");
        } else {
            log.info("User found in the database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            log.info("success catch roles, {}", role.getName());
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(Map<String, String> data) {
        String name = data.get("name");
        String username = data.get("username");
        String password = data.get("password");
        User user = new User(null, null, name, username, password, new ArrayList<>());
        log.info("Saving new user {} to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.insertUser(user);
        user.getRoles().forEach(role -> userRepo.addRoleToUserById(user.getId(), role.getId()));
        return user;
    }

    @Override
    public Map<String, String> register(String username, String name, String password, String confirmedPassword) {
        Map<String, String> data = new HashMap<>();
        if (username == null) {
            data.put("error_message", "用户名不能为空");
            return data;
        }
        if (password == null || confirmedPassword == null) {
            data.put("error_message", "密码不能为空");
            return data;
        }

        if (!password.equals(confirmedPassword)) {
            data.put("error_message", "两次密码不相同");
            return data;
        }

        username = username.trim();
        if (username.length() < 3) {
            data.put("error_message", "用户名过短");
            return data;
        }
        User userInDatabase = userRepo.findByUsername(username);
        if (userInDatabase != null) {
            data.put("error_message", "用户名已存在");
            return data;
        }

        User user = new User(null, null, name, username, password, new ArrayList<>());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("--------------------------------------------------------------");
        userRepo.insertUser(user);
        log.info("Saving new user {} to the database", user.getName());
        userInfoRepo.insert(new UserInfo(user.getId(), "https://i.pinimg.com/564x/4a/71/30/4a71306d68989d164b800c84964bec67.jpg"));
        addRoleToUser(username, "ROLE_USER");
        data.put("error_message", "success");
        return data;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        System.out.println("------------------------------------------------");
        System.out.println(role);
        roleRepo.insert(role);
        return role;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding tole {} to user {}", roleName, username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        System.out.println(user);
        System.out.println(role);
        userRepo.addRoleToUserById(user.getId(), role.getId());
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching users");
        return userRepo.findAll();
    }

    @Override
    public Map<String, Object> getinfo(HttpServletRequest request) {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = jwt.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        User user = getUser(username);
        UserInfo userInfo = userInfoRepo.selectById(user.getId());
        String photo = userInfo.getPhoto();
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId().toString());
        data.put("username", username);
        data.put("photo", photo);
        data.put("error_message", "success");
        return data;
    }
}
