package ru.andreybaryshnikov.otus_2_14.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andreybaryshnikov.otus_2_14.exception.*;
import ru.andreybaryshnikov.otus_2_14.model.User;
import ru.andreybaryshnikov.otus_2_14.model.dto.UserDto;
import ru.andreybaryshnikov.otus_2_14.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private static final ModelMapper modelMapper = new ModelMapper();
    private static Random r = new Random();
    private final UserRepository userRepository;

    @Transactional
    @Override
    public User saveUser(UserDto userDto) {
        User user;
        if (userDto != null) {
            user = modelMapper.map(userDto, User.class);
            validate(user);
            user = userRepository.save(user);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            user = new User();
            Integer randI = r.nextInt(2_000_000_000);
            user.setName("user_" + randI);
            randI = r.nextInt(2_000_000_000);
            user.setEmail("user_" + randI + "@mail.ru");
            validate(user);
            user = userRepository.save(user);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return user;
    }

    @Transactional
    @Override
    public User updateUser(long userId, User updateUser) {
        validateForUpdateUser(userId, updateUser);
        User user = userRepository.findById(userId).orElseThrow();
        if(updateUser.getName() != null)
            user.setName(updateUser.getName());
        if(updateUser.getEmail() != null)
            user.setEmail(updateUser.getEmail());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(long userId) {
        userIdValidate(userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow();
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        if ( userId != 0) {
            userIdValidate(userId);
            userRepository.deleteById(userId);
        } else {
            long userId2 = getMaxId();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (userId2 > 3)
                userRepository.deleteById(userId2);
        }
    }

    private long getMaxId() {
        List<User> users = userRepository.findAll();
        long id = 0;
        for (User user : users)
            if (id < user.getId()) id = user.getId();
        return id;
    }

    private void validate(User user){
        if(user.getEmail() == null || user.getEmail().isBlank())
            throw new BadRequestException();
        if(!user.getEmail().contains("@"))
            throw new BadRequestException();
    }

    private void validateForUpdateUser(long userId, User user) {
        userIdValidate(userId);
        if(user.getEmail() != null){
            emailValidate(user.getEmail());
        };
    }

    private void emailValidate(String email){
        List<User> users = userRepository.findAll();
        if(users.stream().allMatch(user -> user.getEmail().equals(email))){
            throw new InternalServerError();
        }
    }
    private void userIdValidate(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User not found, id = " + userId);
        }
    }
}
