package ru.practicum.explore.controller.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return userService.getUsers(ids, from, size);
    }

}
