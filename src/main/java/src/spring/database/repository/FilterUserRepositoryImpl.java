package src.spring.database.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import src.spring.database.entity.Role;
import src.spring.database.entity.User;
import src.spring.dto.PersonalInfo;
import src.spring.dto.UserFilter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository{

    private final EntityManager entityManager;
    //JDBC
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private static final String FIND_BY_COMPANY_AND_ROLE = """
            SELECT firstname,lastname,birth_date
            FROM users
            WHERE company_id = ? AND role = ?;
            """;

    private static final String UPDATE_COMPANY_AND_ROLE = """
            UPDATE users
            SET company_id = ?, role = ?
            WHERE id = ?
            """;

    private static final String UPDATE_COMPANY_AND_ROLE_NAMED = """
            UPDATE users
            SET company_id = :companyId, role = :role
            WHERE id = :id
            """;

    @Override
    public List<User> findAllByFilter(UserFilter userFilter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        var criteria = cb.createQuery(User.class);

        Root<User> user = criteria.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (userFilter.firstname() != null) {
            predicates.add(cb.like(user.get("firstname"), userFilter.firstname()));
        }
        if (userFilter.lastname() != null) {
            predicates.add(cb.like(user.get("lastname"), userFilter.lastname()));
        }
        if (userFilter.birthDay() != null) {
            predicates.add(cb.lessThan(user.get("birthDay"), userFilter.birthDay()));
        }



        criteria.select(user).where(predicates.toArray(Predicate[]::new));


        return entityManager.createQuery(criteria).getResultList();
    }

    //JDBC

    @Override
    public List<PersonalInfo> findAllByCompanyIdandRole(Integer companyId, Role role) {
        return jdbcTemplate.query(FIND_BY_COMPANY_AND_ROLE,
                (rs, rowNum) -> new PersonalInfo(
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getDate("birth_date").toLocalDate()
        ),
                companyId, role.name());
    }

    //с помощью batch size обновим сразу всех, не отсылается каждый раз новый запрос, а одним
    @Override
    public void updateCompanyAndRole(List<User> users) {
        var args = users.stream()
                .map(user -> new Object[] {user.getCompany().getId(), user.getRole().name(), user.getId()})
                .toList();
        jdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE, args);
    }

    @Override
    public void updateCompanyAndRoleNamed(List<User> users) {
        var args = users.stream()
                .map(user -> Map.of(
                            "companyId", user.getCompany().getId(),
                            "role", user.getRole().name(),
                            "id", user.getId()
                )).map(MapSqlParameterSource::new)
                .toArray(MapSqlParameterSource[]::new);
        namedJdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE_NAMED, args);

    }


}
