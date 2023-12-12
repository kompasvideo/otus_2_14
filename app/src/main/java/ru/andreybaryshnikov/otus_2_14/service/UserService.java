package ru.andreybaryshnikov.otus_2_14.service;

import ru.andreybaryshnikov.otus_2_14.model.User;
import ru.andreybaryshnikov.otus_2_14.model.dto.UserDto;

public interface UserService {
    User saveUser(UserDto userDto);
    User updateUser(long userId, User updateUser);
    User getUser(long userId);
    void deleteUser(long userId);
}
