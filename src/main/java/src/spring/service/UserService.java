package src.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.spring.database.repository.CompanyRepository;
import src.spring.database.repository.UserRepository;
import src.spring.database.entity.Company;
import src.spring.dto.UserCreateEditDto;
import src.spring.dto.UserReadDto;
import src.spring.mapper.UserCreareEditMapper;
import src.spring.mapper.UserReadMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //делаем транзакционность на уровне сервисов
//т.к мы можем делать запросы к базе, к примеру, из мапперов, поэтому перед каждым методом будет открываться транзакция
//лучше сделать readOnly в true, будет оптимизированней, там где не нужно отдельно над методом поставить
public class UserService {

    @Qualifier("userRepository")
    private final UserRepository userRepository;

    private final UserReadMapper userReadMapper;

    private final UserCreareEditMapper userCreareEditMapper;

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .toList();
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional//здесь нам надо readOnly в false, т.к мы создаем сущность
    public UserReadDto create(UserCreateEditDto user) {
        return Optional.of(user)
                .map(userCreareEditMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) {

        //saveAndFlush, потому что иначе не будет запроса в бд, до конца транзакции
        //так сразу flush сделает
        return userRepository.findById(id)
                .map(entity -> userCreareEditMapper.map(userDto, entity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);

    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);

    }

}
