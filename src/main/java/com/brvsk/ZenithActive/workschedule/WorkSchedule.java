package com.brvsk.ZenithActive.workschedule;

import com.brvsk.ZenithActive.user.employee.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;
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

    @Column(name = "year_month")
    private YearMonth yearMonth;

    @ElementCollection(targetClass = WorkShift.class)
    @CollectionTable(name = "work_schedule_shifts", joinColumns = @JoinColumn(name = "work_schedule_id"))
    @Column(name = "shift")
    @Enumerated(EnumType.STRING)
    private List<WorkShift> shifts;

}
