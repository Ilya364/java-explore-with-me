package ru.practicum.ewm.user.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.validation.NullOrNotBlank;
import ru.practicum.ewm.validation.groups.Creation;
import ru.practicum.ewm.validation.groups.Updating;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewUserRequest {
    @NotBlank(groups = {Creation.class})
    @NullOrNotBlank(groups = {Updating.class})
    @Email(groups = {Creation.class, Updating.class})
    @Size(min = 6, max = 254, groups = {Creation.class, Updating.class})
    private String email;
    @NotBlank(groups = {Creation.class})
    @NullOrNotBlank(groups = {Updating.class})
    @Size(min = 2, max = 250, groups = {Creation.class, Updating.class})
    private String name;
}
