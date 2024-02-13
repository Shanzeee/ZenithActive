package com.brvsk.ZenithActive.user.instructor;

import com.brvsk.ZenithActive.user.User;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.user.UserRepository;
import com.brvsk.ZenithActive.user.employee.Employee;
import com.brvsk.ZenithActive.user.employee.EmployeeRepository;
import com.brvsk.ZenithActive.user.employee.EmployeeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService{

    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void createNewInstructorFromEmployee(InstructorCreateRequest request){
        UUID userId = request.getUserId();
        Employee employee = employeeRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (employee instanceof Instructor) {
            throw new IllegalArgumentException("employee with id: "+userId+" is already an instructor");
        }

        Instructor instructor = toInstructor(employee,request);

        employeeRepository.delete(employee);
        instructorRepository.save(instructor);
    }

    @Override
    @Transactional
    public void deleteInstructor(UUID instructorId){
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new UserNotFoundException(instructorId));

        User user = toUser(instructor);
        instructorRepository.deleteById(instructorId);
        userRepository.save(user);
    }

    private Instructor toInstructor(Employee employee, InstructorCreateRequest request){
        Instructor instructor = new Instructor();
        instructor.setUserId(request.getUserId());
        instructor.setFirstName(employee.getFirstName());
        instructor.setLastName(employee.getLastName());
        instructor.setGender(employee.getGender());
        instructor.setEmployeeType(EmployeeType.INSTRUCTOR);
        instructor.setAdditionalInformation(employee.getAdditionalInformation());
        instructor.setHireDate(employee.getHireDate());
        instructor.setDescription(request.getDescription());
        instructor.setSpecialities(request.getSpecialities());
        return instructor;
    }
    private User toUser(Instructor instructor){
        return User.builder()
                .userId(instructor.getUserId())
                .firstName(instructor.getFirstName())
                .lastName(instructor.getLastName())
                .gender(instructor.getGender())
                .build();
    }




}
