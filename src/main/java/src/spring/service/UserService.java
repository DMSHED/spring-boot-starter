package src.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import src.spring.database.repository.CompanyRepository;
import src.spring.database.repository.UserRepository;
import src.spring.database.entity.Company;
import src.spring.dto.UserCreateEditDto;
import src.spring.dto.UserFilter;
import src.spring.dto.UserReadDto;
import src.spring.mapper.UserCreareEditMapper;
import src.spring.mapper.UserReadMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //делаем транзакционность на уровне сервисов
//т.к мы можем делать запросы к базе, к примеру, из мапперов, поэтому перед каждым методом будет открываться транзакция
//лучше сделать readOnly в true, будет оптимизированней, там где не нужно отдельно над методом поставить
public class UserService implements UserDetailsService {

    @Qualifier("userRepository")
    private final UserRepository userRepository;

    private final UserReadMapper userReadMapper;

    private final UserCreareEditMapper userCreareEditMapper;

    private final ImageService imageService;

    public List<UserReadDto> findAll(UserFilter filter) {
        return userRepository.findAllByFilter(filter).stream()
                .map(userReadMapper::map)
                .toList();
    }

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
                .map(dto -> {
                    uploadImage(dto.getImage());
                    return userCreareEditMapper.map(dto);
                })
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userDto) {

        //saveAndFlush, потому что иначе не будет запроса в бд, до конца транзакции
        //так сразу flush сделает
        return userRepository.findById(id)
                .map(entity -> {
                    uploadImage(userDto.getImage());
                    return userCreareEditMapper.map(userDto, entity);
                })
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);

    }

    private void uploadImage(MultipartFile image){
        if ( !image.isEmpty() ) {
            try {
                imageService.upload(image.getOriginalFilename(), image.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

    //UserDetailsService - может быть одно на приложение
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
