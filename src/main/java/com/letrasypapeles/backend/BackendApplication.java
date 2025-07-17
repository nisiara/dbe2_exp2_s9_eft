package com.letrasypapeles.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

		// @Autowired
		// RoleRepository roleRepository;

		// @Bean
    // CommandLineRunner init(RoleRepository roleRepository) {
    //     return args -> {

		// 			Role admin = Role.builder()
		// 			.roleName(ERole.valueOf(ERole.ADMIN.name()))
		// 			.build();
						
		// 			roleRepository.save(admin);
		
		// 			Role developer = Role.builder()
		// 			.roleName(ERole.valueOf(ERole.DEVELOPER.name()))
		// 			.build();
						
		// 			roleRepository.save(developer);

		// 			Role compra = Role.builder()
		// 			.roleName(ERole.valueOf(ERole.COMPRA.name()))
		// 			.build();
						
		// 			roleRepository.save(compra);
		// 		};

		// 	};

}
