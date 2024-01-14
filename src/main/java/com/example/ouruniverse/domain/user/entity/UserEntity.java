package com.example.ouruniverse.domain.user.entity;

import com.example.ouruniverse.domain.school.entity.SchoolEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "grade")
    private Integer grade;

    @ElementCollection
    @Column(name = "follows")
    private List<Long> follows = new ArrayList<>();

    @ElementCollection
    @Column(name = "follwings")
    private List<Long> followings = new ArrayList<>();

    @OneToOne(mappedBy = "userId")
    private SchoolEntity schoolId;
}
