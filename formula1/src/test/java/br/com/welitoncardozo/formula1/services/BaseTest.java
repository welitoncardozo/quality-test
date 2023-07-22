package br.com.welitoncardozo.formula1.services;

import br.com.welitoncardozo.formula1.services.impl.ChampionshipServiceImpl;
import br.com.welitoncardozo.formula1.services.impl.CountryServiceImpl;
import br.com.welitoncardozo.formula1.services.impl.PilotRaceServiceImpl;
import br.com.welitoncardozo.formula1.services.impl.PilotServiceImpl;
import br.com.welitoncardozo.formula1.services.impl.RaceServiceImpl;
import br.com.welitoncardozo.formula1.services.impl.SpeedwayServiceImpl;
import br.com.welitoncardozo.formula1.services.impl.TeamServiceImpl;
import br.com.welitoncardozo.formula1.services.impl.UserServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@SpringBootTest
@ActiveProfiles("test")
abstract class BaseTest {
	@Bean
	UserService userService() {
		return new UserServiceImpl();
	}

	@Bean
	TeamService teamService() {
		return new TeamServiceImpl();
	}

	@Bean
	ChampionshipService championshipService() {
		return new ChampionshipServiceImpl();
	}

	@Bean
	CountryService countryService() {
		return new CountryServiceImpl();
	}

	@Bean
	PilotService pilotService() {
		return new PilotServiceImpl();
	}

	@Bean
	PilotRaceService pilotRaceService() {
		return new PilotRaceServiceImpl();
	}

	@Bean
	RaceService raceService() {
		return new RaceServiceImpl();
	}

	@Bean
	SpeedwayService speedwayService() {
		return new SpeedwayServiceImpl();
	}
}
