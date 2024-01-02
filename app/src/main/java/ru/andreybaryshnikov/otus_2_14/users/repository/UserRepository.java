package ru.andreybaryshnikov.otus_2_14.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andreybaryshnikov.otus_2_14.users.model.UserProfile;

public interface UserRepository extends JpaRepository<UserProfile,Long> {
}
