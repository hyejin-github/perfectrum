package com.perfectrum.backend.domain.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "accords")
public class AccordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(name = "accord_name")
    private String accordName;
    @Column(name = "accord_description")
    private String accordDescription;
    @Column(name = "accord_img")
    private String accordImg;

    @ManyToOne
    @JoinColumn(name = "accord_class")
    AccordClassEntity accordClass;
}
