package br.com.welitoncardozo.formula1.services;

import java.util.List;

import br.com.welitoncardozo.formula1.models.Country;
import br.com.welitoncardozo.formula1.models.Pilot;
import br.com.welitoncardozo.formula1.models.Team;

public interface PilotService {

	Pilot findById(Integer id);

	Pilot insert(Pilot pilot);

	List<Pilot> listAll();

	Pilot update(Pilot pilot);

	void delete(Integer id);

	List<Pilot> findByNameStartsWithIgnoreCase(String name);

	List<Pilot> findByCountry(Country country);

	List<Pilot> findByTeam(Team team);

}
