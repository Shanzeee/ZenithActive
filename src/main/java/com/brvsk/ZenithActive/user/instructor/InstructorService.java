package com.brvsk.ZenithActive.user.instructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface InstructorService {
    @Transactional
    void createNewInstructor(InstructorCreateRequest request);

    @Transactional
    void deleteInstructor(UUID instructorId);
}
