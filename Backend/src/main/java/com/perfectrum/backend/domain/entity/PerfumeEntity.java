package com.perfectrum.backend.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name="perfumes")
public class PerfumeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(name="brand_name")
    private String brandName;
    @Column(name="perfume_name")
    private String perfumeName;
    private String concentration;
    private String gender;
    private String scent;
    @Column(name="top_notes")
    private String topNotes;
    @Column(name="middle_notes")
    private String middleNotes;
    @Column(name="base_notes")
    private String baseNotes;
    @Column(name="item_rating")
    private Float itemRating;
    @Column(name="perfume_img")
    private String perfumeImg;
    private String description;
    private String seasons;
    private String timezone;
    private Integer longevity;
    private Integer sillage;
    @Column(name="wish_count")
    private Integer wishCount;
    @Column(name="have_count")
    private Integer haveCount;
}
