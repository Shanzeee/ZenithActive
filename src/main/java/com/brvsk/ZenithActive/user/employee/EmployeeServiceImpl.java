package com.brvsk.ZenithActive.user.employee;

import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;


    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getEmployeeById(UUID userId) {
        return employeeRepository.findById(userId)
                .map(employeeMapper::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void deleteEmployee(UUID userId) {
        employeeRepository.deleteById(userId);
    }



}
