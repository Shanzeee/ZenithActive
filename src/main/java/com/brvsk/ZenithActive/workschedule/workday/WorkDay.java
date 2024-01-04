package com.brvsk.ZenithActive.workschedule.workday;

import com.brvsk.ZenithActive.workschedule.WorkSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Table(name = "work_day")
@Entity(name = "WorkDay")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "work_schedule_id")
    private WorkSchedule workSchedule;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "additional_information")
    private String additionalInformation;

}
