package com.brvsk.ZenithActive.user.employee;

import com.brvsk.ZenithActive.user.EmailAlreadyExistsException;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserRepository userRepository;


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
    public void createEmployee(EmployeeCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Employee newEmployee = toEntity(request);
        employeeRepository.save(newEmployee);

    }

    @Override
    public void deleteEmployee(UUID userId) {
        employeeRepository.deleteById(userId);
    }

    public Employee toEntity(EmployeeCreateRequest request) {
        Employee employee = new Employee();
        employee.setUserId(UUID.randomUUID());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setGender(request.getGender());
        employee.setEmail(request.getEmail());
        employee.setEmployeeType(request.getEmployeeType());
        employee.setHireDate(LocalDate.now());
        employee.setAdditionalInformation(request.getAdditionalInformation());
        return employee;
    }


}
