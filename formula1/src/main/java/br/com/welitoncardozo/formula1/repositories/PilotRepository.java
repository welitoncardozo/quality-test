package br.com.welitoncardozo.formula1.repositories;

import java.util.List;

import br.com.welitoncardozo.formula1.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.welitoncardozo.formula1.models.Country;
import br.com.welitoncardozo.formula1.models.Pilot;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Integer>{
	
	List<Pilot> findByNameStartsWithIgnoreCase(String name);
	List<Pilot> findByCountry(Country country);
	List<Pilot> findByTeam(Team team);
	
	
}
