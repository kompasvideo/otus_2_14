package ru.andreybaryshnikov.otus_2_14.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andreybaryshnikov.otus_2_14.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
