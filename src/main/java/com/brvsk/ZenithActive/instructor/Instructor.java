package com.brvsk.ZenithActive.instructor;

import com.brvsk.ZenithActive.course.Course;
import com.brvsk.ZenithActive.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "Instructor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instructor extends User {

    private String description;
    private List<Speciality> specialities;
    @OneToMany
    private List<Course> courses;



}
