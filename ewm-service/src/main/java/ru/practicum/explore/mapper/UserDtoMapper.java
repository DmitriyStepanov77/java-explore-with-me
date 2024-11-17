package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.model.User;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    public User toEntity(UserDto userDto);

    public UserDto toModel(User user);
}
