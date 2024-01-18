package com.example.ouruniverse.domain.school.entity;

import com.example.ouruniverse.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity userId;

    private String schoolName;

    private String provincialOfficeOfEducationCode;

    private Integer administrativeStandardCode;

    private String schoolPosition;

    private String establishmentName;
}
