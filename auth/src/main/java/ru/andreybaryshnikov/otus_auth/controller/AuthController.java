package ru.andreybaryshnikov.otus_auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.andreybaryshnikov.otus_auth.exception.UnauthorizedException;
import ru.andreybaryshnikov.otus_auth.model.LoginDto;
import ru.andreybaryshnikov.otus_auth.model.User;
import ru.andreybaryshnikov.otus_auth.model.UserDto;
import ru.andreybaryshnikov.otus_auth.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private static final Map<String, User> sessions = new HashMap<>();
    private static final Random random = new Random();
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public User register(@RequestBody UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return userRepository.save(user);
    }


    private String generateSessionId(int size) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private String createSession(User userInfo) {
        String sessionId = generateSessionId(40);
        sessions.put(sessionId, userInfo);
        return sessionId;
    }

    private User getUserByCredentials(LoginDto loginDto) {
        return userRepository.findByLoginAndPassword(loginDto.getLogin(), loginDto.getPassword());
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        User userInfo = getUserByCredentials(loginDto);
        if (userInfo == null)
            throw new UnauthorizedException();
        String sessionId = createSession(userInfo);
        response.addCookie(new Cookie("session_id", sessionId));
        return "{\"IDtoken\": \"" + sessionId + "\"}";
    }

    @GetMapping("/sessions")
    public Map<String, User> sessions() {
        return sessions;
    }

    @GetMapping("/signin")
    public String signin() {
        return "{\"message\": \"Please go to login and provide Login/Password\"}";
    }

    @GetMapping("/auth")
    public User auth(@CookieValue(value = "session_id", required = false) String sessionId,
                     HttpServletResponse response) {
        if (sessionId != null && sessions.containsKey(sessionId)) {
            User data = sessions.get(sessionId);
            response.setHeader("X-UserId", String.valueOf(data.getId()));
            response.setHeader("X-User", data.getLogin());
            response.setHeader("X-Email", data.getEmail());
            response.setHeader("X-First-Name", data.getFirst_name());
            response.setHeader("X-Last-Name", data.getLast_name());
            response.setStatus(HttpStatus.OK.value());
            return data;
        }
        throw new UnauthorizedException();
    }

    @RequestMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("session_id", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}