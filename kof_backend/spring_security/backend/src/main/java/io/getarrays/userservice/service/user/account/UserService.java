package io.getarrays.userservice.service.user.account;

import io.getarrays.userservice.domain.Role;
import io.getarrays.userservice.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {
    User saveUser(Map<String, String> data);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
    Map<String, Object> getinfo(HttpServletRequest request);

    Map<String, String> register(String username, String name, String password, String confirmedPassword);

}
