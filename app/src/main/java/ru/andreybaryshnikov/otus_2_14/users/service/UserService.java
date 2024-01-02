package ru.andreybaryshnikov.otus_2_14.users.service;

import ru.andreybaryshnikov.otus_2_14.users.model.UserProfile;

public interface UserService {
    UserProfile updateUser(long userId, UserProfile updateUser);
    UserProfile getUserProfileById(long id);
}
