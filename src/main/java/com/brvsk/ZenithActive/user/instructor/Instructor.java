package com.brvsk.ZenithActive.user.instructor;

import com.brvsk.ZenithActive.review.instructor.ReviewInstructor;
import com.brvsk.ZenithActive.session.Session;
import com.brvsk.ZenithActive.user.employee.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Instructor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instructor extends Employee {

    @Column(length = 1000)
    private String description;

    @Enumerated
    private List<Speciality> specialities;

    @OneToMany(mappedBy = "instructor")
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "instructor")
    private Set<ReviewInstructor> reviews = new HashSet<>();
}
