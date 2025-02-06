package src.spring.database.repository;

import src.spring.database.entity.Role;
import src.spring.database.entity.User;
import src.spring.dto.PersonalInfo;
import src.spring.dto.UserFilter;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilter userFilter);

    // JDBC
    //идет в обход hibernate и его persistence context
    //поэтому работает только с dto

    List<PersonalInfo> findAllByCompanyIdandRole(Integer companyId, Role role);

    //с помощью batch size обновим сразу всех, не отсылается каждый раз новый запрос, а одним
    void updateCompanyAndRole(List<User> users);

    void updateCompanyAndRoleNamed(List<User> users);
}
