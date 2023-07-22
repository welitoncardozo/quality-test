package br.com.welitoncardozo.formula1.services;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.welitoncardozo.formula1.models.Championship;
import br.com.welitoncardozo.formula1.models.Race;
import br.com.welitoncardozo.formula1.models.Speedway;

public interface RaceService {

	Race findById(Integer id);

	Race insert(Race race);

	List<Race> listAll();

	Race update(Race race);

	void delete(Integer id);

	List<Race> findByDate(ZonedDateTime date);

	List<Race> findBySpeedway(Speedway speedway);

	List<Race> findByChampionship(Championship championship);

}
