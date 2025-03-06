package src.spring.database.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = "userChats") //исключаем чтобы не было зацикливания
@EqualsAndHashCode(of = "username", callSuper = false) //делаем hashcode на основе username
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users") //так называется в БД
//нужна для отслеживания какие поля были изменены. для Revision
//NOT_AUDITED нужна, чтобы не отслеживать зависимые сущности
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class User extends AuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "image")
    private String image;

    @Column(name = "lastname")
    private String lastName;

    @Enumerated(EnumType.STRING) //чтобы в бд сохранялось строкой, а не числом
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotAudited //над коллекциями нужно явно ставить аннотацию не аудировать
    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();
}
