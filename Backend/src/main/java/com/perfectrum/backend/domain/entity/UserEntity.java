package com.perfectrum.backend.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(name="user_id")
    private String userId;
    @Column(name="profile_img")
    private String profileImg;
    private String nickname;
    private String gender;
    private String seasons;
    @Column(name="accord_class")
    private Integer accordClass;

    @Builder
    public UserEntity(String userId, String nickname){
        this.userId = userId;
        this.nickname = nickname;
    }
}