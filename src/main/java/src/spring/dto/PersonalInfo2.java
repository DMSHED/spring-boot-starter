package src.spring.dto;

import org.springframework.beans.factory.annotation.Value;

public interface PersonalInfo2 {

    String getFirstname();

    String getLastname();

    String getBirthday();

    @Value("#{target.firstname + ' ' + target.lastname}")
    String getFullName();
}
