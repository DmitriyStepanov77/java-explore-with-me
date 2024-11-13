package ru.practicum.explore.service.user;

import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.model.User;

import java.util.List;

public interface UserService {

    public UserDto addUser(UserDto userDto);

    public List<UserDto> getUsers(List<Integer> ids, int from, int size);

    public void deleteUser(int userId);

    public User getUser(int userId);
}
