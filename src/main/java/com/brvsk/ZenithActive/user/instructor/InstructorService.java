package com.brvsk.ZenithActive.user.instructor;

import java.util.UUID;

public interface InstructorService {
    void createNewInstructor(InstructorCreateRequest request);
    void deleteInstructor(UUID instructorId);
}
