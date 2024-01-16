package com.example.ouruniverse.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Grade {
    GRADE_1(1),
    GRADE_2(2),
    GRADE_3(3);

    private final int grade;
}
