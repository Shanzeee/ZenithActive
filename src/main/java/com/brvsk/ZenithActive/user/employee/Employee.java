package com.brvsk.ZenithActive.user.employee;

import com.brvsk.ZenithActive.user.User;
import com.brvsk.ZenithActive.workschedule.WorkSchedule;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "Employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User {

    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;
    private LocalDate hireDate;
    private String additionalInformation;

    @OneToMany(mappedBy = "employee")
    private List<WorkSchedule> workSchedules;
}
