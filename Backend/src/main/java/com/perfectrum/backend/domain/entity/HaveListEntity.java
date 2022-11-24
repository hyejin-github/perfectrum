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
@Table(name = "have_lists")
public class HaveListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "perfume_idx")
    private PerfumeEntity perfume;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @Builder
    public HaveListEntity(Integer idx, UserEntity user, PerfumeEntity perfume, Boolean isDelete) {
        this.idx = idx;
        this.user = user;
        this.perfume = perfume;
        this.isDelete = isDelete;
    }
}
