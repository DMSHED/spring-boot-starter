package src.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.postgresql.util.LruCache.CreateAction;
import org.springframework.web.multipart.MultipartFile;
import src.spring.database.entity.Role;
import src.spring.validation.UserInfo;

import java.time.LocalDate;

@Value
@FieldNameConstants
@UserInfo
public class UserCreateEditDto {

    @Email
    String username;

    //нужен только на время создания, на обновление пароля отдельный функционал
    @NotBlank(groups = CreateAction.class)
    String rawPassword;

    LocalDate birthDate;

    @Size(min = 3, max = 64)
    String firstname;

    String lastname;

    Role role;

    Integer companyId;

    MultipartFile image;


}
