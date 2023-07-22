package br.com.welitoncardozo.formula1.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.welitoncardozo.formula1.models.Championship;
import br.com.welitoncardozo.formula1.models.Race;
import br.com.welitoncardozo.formula1.models.Speedway;

@Repository
public interface RaceRepository extends JpaRepository<Race, Integer>{
	
	List<Race> findByDate(ZonedDateTime date);
	List<Race> findBySpeedway(Speedway speedway);
	List<Race> findByChampionship(Championship championship);

}
