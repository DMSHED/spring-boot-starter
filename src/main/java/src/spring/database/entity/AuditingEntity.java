package src.spring.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass //чтобы поля были унаследованы дочерней сущностью
/*
чтобы поля автоматически обновлялись, нужно использовать Listeners
 */
@EntityListeners(AuditingEntityListener.class) //листнер в спринге создается автоматически
public abstract class AuditingEntity<T extends Serializable> implements BaseEntity<T> {

    @CreatedDate
//    @Column(name = "create_at")
    private Instant createAt; //время на уровне java приложения - это timestamp

    @LastModifiedDate
//    @Column(name = "modified_at")
    private Instant modifiedAt;

    @CreatedBy
    private String createBy;

    @LastModifiedBy
    private String modifiedBy;
}
