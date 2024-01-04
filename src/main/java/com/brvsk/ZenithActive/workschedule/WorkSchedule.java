package com.brvsk.ZenithActive.workschedule;

import com.brvsk.ZenithActive.user.employee.Employee;
import com.brvsk.ZenithActive.utils.Months;
import com.brvsk.ZenithActive.workschedule.workday.WorkDay;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name = "work_schedule")
@Entity(name = "WorkSchedule")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID workScheduleId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "work_year")
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_month")
    private Months month;

    @OneToMany(mappedBy = "workSchedule")
    private List<WorkDay> workDays;

}
