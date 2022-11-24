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
@Table(name = "user_search_logs")
public class UserSearchLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private UserEntity user;

    private String gender;
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "accord_class")
    private AccordClassEntity accordClass;

    @Builder
    public UserSearchLogEntity(Integer idx, UserEntity user, String gender, Integer duration, AccordClassEntity accordClass) {
        this.idx = idx;
        this.user = user;
        this.gender = gender;
        this.duration = duration;
        this.accordClass = accordClass;
    }
}
