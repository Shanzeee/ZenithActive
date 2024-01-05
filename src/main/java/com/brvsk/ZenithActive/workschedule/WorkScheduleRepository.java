package com.brvsk.ZenithActive.workschedule;

import com.brvsk.ZenithActive.user.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, UUID> {

    Optional<List<WorkSchedule>> findWorkSchedulesByEmployee (Employee employee);
    Optional<WorkSchedule> findWorkScheduleByEmployee_UserIdAndYearMonth(UUID employeeId, YearMonth yearMonth);
    List<WorkSchedule> findWorkSchedulesByYearMonth(YearMonth yearMonth);
    boolean existsByEmployee_UserIdAndYearMonth(UUID employeeId, YearMonth now);
}
