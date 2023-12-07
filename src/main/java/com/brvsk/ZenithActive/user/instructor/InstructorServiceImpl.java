package com.brvsk.ZenithActive.user.instructor;

import com.brvsk.ZenithActive.user.User;
import com.brvsk.ZenithActive.user.UserNotFoundException;
import com.brvsk.ZenithActive.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService{

    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;

    @Override
    @Transactional
    public void createNewInstructor(InstructorCreateRequest request){
        var userId = request.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (user instanceof Instructor) {
            throw new IllegalArgumentException("user with id: "+userId+" is already an instructor");
        }

        Instructor instructor = toInstructor(user,request);

        userRepository.delete(user);
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

    private Instructor toInstructor(User user, InstructorCreateRequest request){
        Instructor instructor = new Instructor();
        instructor.setUserId(request.getUserId());
        instructor.setFirstName(user.getFirstName());
        instructor.setLastName(user.getLastName());
        instructor.setGender(user.getGender());
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
