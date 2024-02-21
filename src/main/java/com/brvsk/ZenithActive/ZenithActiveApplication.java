package com.brvsk.ZenithActive;

import com.brvsk.ZenithActive.security.auth.AuthenticationService;
import com.brvsk.ZenithActive.security.auth.RegisterEmployeeRequest;
import com.brvsk.ZenithActive.security.auth.RegisterInstructorRequest;
import com.brvsk.ZenithActive.security.auth.RegisterMemberRequest;
import com.brvsk.ZenithActive.user.Gender;
import com.brvsk.ZenithActive.user.employee.EmployeeType;
import com.brvsk.ZenithActive.user.instructor.Speciality;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ZenithActiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZenithActiveApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService authenticationService) {

		return args -> {

			RegisterMemberRequest member = RegisterMemberRequest
					.builder()
					.firstName("member")
					.lastName("member")
					.email("member@example.com")
					.password("member")
					.gender(Gender.OTHER)
					.height(185)
					.weight(75)
					.build();
			System.out.println("Member token: " +
					authenticationService.registerMember(member).getAccessToken());

			RegisterEmployeeRequest employee = RegisterEmployeeRequest
					.builder()
					.firstName("employee")
					.lastName("employee")
					.email("employee@example.com")
					.password("employee")
					.gender(Gender.OTHER)
					.employeeType(EmployeeType.CLEANER)
					.additionalInformation("employee")
					.build();
			System.out.println("Employee token: " +
					authenticationService.registerEmployee(employee).getAccessToken());

			RegisterInstructorRequest instructor = RegisterInstructorRequest
					.builder()
					.firstName("instructor")
					.lastName("instructor")
					.email("instructor@example.com")
					.password("instructor")
					.gender(Gender.OTHER)
					.employeeType(EmployeeType.INSTRUCTOR)
					.additionalInformation("instructor")
					.description("instructor")
					.specialities(List.of(Speciality.YOGA, Speciality.PILATES))
					.build();

			System.out.println("Instructor token: " +
					authenticationService.registerInstructor(instructor).getAccessToken());

			System.out.println("Admin token: " + authenticationService.createAdmin().getAccessToken());

		};
	}

}
