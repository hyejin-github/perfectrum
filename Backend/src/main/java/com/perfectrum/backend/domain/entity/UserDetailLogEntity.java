package com.perfectrum.backend.domain.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_detail_logs")
public class UserDetailLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "perfume_idx")
    private PerfumeEntity perfume;

    @Builder
    public UserDetailLogEntity(Integer idx, UserEntity user,PerfumeEntity perfume){
        this.idx = idx;
        this.user = user;
        this.perfume = perfume;
    }

}
