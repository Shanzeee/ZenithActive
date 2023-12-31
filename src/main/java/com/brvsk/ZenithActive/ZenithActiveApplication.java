package com.brvsk.ZenithActive;

import com.brvsk.ZenithActive.security.auth.AuthenticationService;
import com.brvsk.ZenithActive.security.auth.RegisterEmployeeRequest;
import com.brvsk.ZenithActive.security.auth.RegisterMemberRequest;
import com.brvsk.ZenithActive.user.Gender;
import com.brvsk.ZenithActive.user.employee.EmployeeType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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

		};
	}

}
