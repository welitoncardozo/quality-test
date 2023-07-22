package br.com.welitoncardozo.formula1;

import br.com.welitoncardozo.formula1.services.TeamService;
import br.com.welitoncardozo.formula1.services.UserService;
import br.com.welitoncardozo.formula1.services.impl.TeamServiceImpl;
import br.com.welitoncardozo.formula1.services.impl.UserServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class BaseTest {
	@Bean
	public UserService userService() {
		return new UserServiceImpl();
	}

	@Bean
	public TeamService teamService() {
		return new TeamServiceImpl();
	}
}
