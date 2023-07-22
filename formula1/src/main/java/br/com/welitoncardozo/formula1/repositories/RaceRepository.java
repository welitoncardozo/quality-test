package br.com.welitoncardozo.formula1.repositories;

import br.com.welitoncardozo.formula1.models.Championship;
import br.com.welitoncardozo.formula1.models.Race;
import br.com.welitoncardozo.formula1.models.Speedway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceRepository extends JpaRepository<Race, Integer>{
	@Query("SELECT e FROM corrida e WHERE year(e.date) = ?1 AND month(e.date) = ?2")
	List<Race> findByDate(final int year, final int month);

	List<Race> findBySpeedway(Speedway speedway);
	List<Race> findByChampionship(Championship championship);
}
