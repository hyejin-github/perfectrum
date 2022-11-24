package com.perfectrum.backend.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "surveys")
public class SurveyEntity{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idx;

    @ManyToOne
    @JoinColumn(name="user_idx")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="perfume_idx")
    private PerfumeEntity perfume;

    @Column(name="like_seasons")
    private String likeSeasons;
    @Column(name="like_gender")
    private String likeGender;
    @Column(name="like_longevity")
    private Integer likeLongevity;
    @Column(name="like_timezone")
    private String likeTimezone;
    @Column(name="like_accord_class")
    private Integer likeAccordClass;

    @Override
    public String toString() {
        return "SurveyEntity{" +
                "idx=" + idx +
                ", user=" + user +
                ", perfume=" + perfume +
                ", likeSeasons='" + likeSeasons + '\'' +
                ", likeGender='" + likeGender + '\'' +
                ", likeLongevity=" + likeLongevity +
                ", likeTimezone='" + likeTimezone + '\'' +
                ", likeAccordClass=" + likeAccordClass +
                '}';
    }
}