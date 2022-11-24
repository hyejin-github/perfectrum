package com.perfectrum.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "user_accord_class")
public class UserAccordClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "accord_class_idx")
    AccordClassEntity accordClass;

    @Column(name = "accord_class_count")
    Integer accordClassCount;

    @Builder
    public UserAccordClassEntity(Integer idx, UserEntity user, AccordClassEntity accordClass, Integer accordClassCount) {
        this.idx =idx;
        this.user = user;
        this.accordClass = accordClass;
        this.accordClassCount = accordClassCount;
    }
}
