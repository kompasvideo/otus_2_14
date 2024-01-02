package ru.andreybaryshnikov.otus_2_14.users.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.andreybaryshnikov.otus_2_14.users.exception.UnauthorizedException;
import ru.andreybaryshnikov.otus_2_14.users.model.User;
import ru.andreybaryshnikov.otus_2_14.users.model.UserProfile;
import ru.andreybaryshnikov.otus_2_14.users.model.dto.UserProfileDto;
import ru.andreybaryshnikov.otus_2_14.users.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/users/me")
    public User me(HttpServletRequest request) {
        String xUserId = request.getHeader("X-UserId");
        if (xUserId == null)
            throw new UnauthorizedException();
        User user = new User();
        user.setId(Long.parseLong(xUserId));
        user.setLogin(request.getHeader("X-User"));
        user.setEmail(request.getHeader("X-Email"));
        user.setFirst_name(request.getHeader("X-First-Name"));
        user.setLast_name(request.getHeader("X-Last-Name"));
        UserProfile userProfile = userService.getUserProfileById(user.getId());
        if (userProfile != null) {
            user.setAvatar_uri(userProfile.getAvatar_uri());
            user.setAge(userProfile.getAge());
        }
        return user;
    }

    @PutMapping("/users/me")
    public UserProfile updateMe(@RequestBody UserProfileDto userProfileDto, HttpServletRequest request) {
        String xUserId = request.getHeader("X-UserId");
        if (xUserId == null)
            throw new UnauthorizedException();
        User user = new User();
        user.setId(Long.parseLong(xUserId));
        user.setLogin(request.getHeader("X-User"));
        user.setEmail(request.getHeader("X-Email"));
        user.setFirst_name(request.getHeader("X-First-Name"));
        user.setLast_name(request.getHeader("X-Last-Name"));
        UserProfile userProfile = modelMapper.map(userProfileDto, UserProfile.class);
        userProfile = userService.updateUser(user.getId(), userProfile);
        return userProfile;
    }
}
