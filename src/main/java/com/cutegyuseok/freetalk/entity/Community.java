package com.cutegyuseok.freetalk.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "community")
public class Community {

    @Id
    @Column(name = "pk")
    private Long pk;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "introduce", nullable = false)
    private String introduce;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @Column(name = "background_image", nullable = false)
    private String backgroundImage;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Builder
    public Community(Long pk, String name, String introduce, String status, String mainImage, String backgroundImage) {
        this.pk = pk;
        this.name = name;
        this.introduce = introduce;
        this.status = status;
        this.mainImage = mainImage;
        this.backgroundImage = backgroundImage;
    }
}
