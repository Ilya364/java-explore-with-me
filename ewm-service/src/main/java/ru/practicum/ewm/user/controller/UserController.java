package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.validation.groups.Creation;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RequestMapping("/admin/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(defaultValue = "0") Long from,
            @Positive @RequestParam(defaultValue = "10") Long size
    ) {
        log.info("Request to receive users.");
        return service.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated({Creation.class}) @RequestBody NewUserRequest dto) {
        log.info("Request to create user.");
        return service.createUser(dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive @PathVariable Long userId) {
        log.info("Request to delete user.");
        service.deleteUser(userId);
    }
}
