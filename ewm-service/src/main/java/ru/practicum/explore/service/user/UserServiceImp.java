package ru.practicum.explore.service.user;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.mapper.UserDtoMapper;
import ru.practicum.explore.model.QUser;
import ru.practicum.explore.model.User;
import ru.practicum.explore.repository.UsersRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class UserServiceImp implements UserService {
    private final UserDtoMapper userDtoMapper;
    private final UsersRepository usersRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("Added user by name = {} and email = {}.", userDto.getName(), userDto.getEmail());
        return userDtoMapper.toModel(usersRepository.save(userDtoMapper.toEntity(userDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        log.info("Get categories by ids = {}.", ids);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (ids != null && !ids.isEmpty())
            booleanBuilder.and(QUser.user.id.in(ids));
        return usersRepository.findAll(booleanBuilder, pageable).getContent().stream()
                .map(userDtoMapper::toModel).toList();
    }

    @Override
    public void deleteUser(int userId) {
        log.info("Delete user by id = {}.", userId);
        usersRepository.deleteById(userId);
    }

    @Override
    public User getUser(int userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User is not found"));
    }
}
