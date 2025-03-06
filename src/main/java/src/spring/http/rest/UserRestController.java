package src.spring.http.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.spring.database.entity.Role;
import src.spring.dto.UserCreateEditDto;
import src.spring.dto.UserFilter;
import src.spring.dto.UserReadDto;
import src.spring.service.UserService;

import java.util.List;


@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
//обьединяет в себе @Controller and @ResponseBody
@RestController // когда ставим то возвращаем как есть из методов
public class UserRestController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserReadDto> findAll(@ModelAttribute("userFilter") UserFilter userFilter) {
        List<UserReadDto> all = userService.findAll(userFilter);

        return all;
    }

    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable("id") Long id) {
        return userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto create(@Validated @RequestBody UserCreateEditDto user) {

        UserReadDto userReadDto = userService.create(user);

        return userReadDto;
    }

    @PutMapping("/{id}")
    public UserReadDto update(@PathVariable Long id,
                         @Validated @RequestBody UserCreateEditDto userDto) {



        return userService.update(id, userDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

}
