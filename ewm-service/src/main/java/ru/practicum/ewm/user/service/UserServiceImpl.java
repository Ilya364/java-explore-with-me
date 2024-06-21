package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.error.exception.DuplicateException;
import ru.practicum.ewm.error.exception.NotFoundException;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

import static ru.practicum.ewm.user.dto.UserDtoMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Long from, Long size) {
        List<UserDto> users;
        if (ids != null) {
            users = toUserDtoList(repository.findAllByIdIn(ids));
            log.info("Users {} received.", ids);
        } else if (from != null && size != null) {
            users = toUserDtoList(repository.findAllByIdBetween(from, from + size));
            log.info("{} users from user {} received.", size, from);
        } else {
            users = toUserDtoList(repository.findAll());
            log.info("All users received.");
        }
        return users;
    }

    @Override
    public UserDto createUser(NewUserRequest dto) {
        try {
            User user = repository.save(toUser(dto));
            log.info("User {} created.", user.getId());
            return toUserDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException(String.format("User with email \"%s\" already exists.", dto.getEmail()));
        }
    }

    @Override
    public void deleteUser(Long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException(String.format("User with if %d not found.", userId));
        }
        repository.deleteById(userId);
        log.info("User {} deleted.", userId);
    }
}