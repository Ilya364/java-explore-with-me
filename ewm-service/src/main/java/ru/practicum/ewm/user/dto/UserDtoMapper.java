package ru.practicum.ewm.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserDtoMapper {
    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public List<UserShortDto> toUserShortDtoList(List<User> users) {
        return users.stream()
                .map(UserDtoMapper::toUserShortDto)
                .collect(Collectors.toList());
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        return users.stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public User toUser(NewUserRequest dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}
