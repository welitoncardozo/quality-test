package br.com.welitoncardozo.formula1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.welitoncardozo.formula1.models.Pilot;
import br.com.welitoncardozo.formula1.models.PilotRace;
import br.com.welitoncardozo.formula1.models.Race;

@Repository
public interface PilotRaceRepository extends JpaRepository<PilotRace, Integer> {
	
	List<PilotRace> findByPlacement(Integer placement);
	List<PilotRace> findByPilot(Pilot pilot);
	List<PilotRace> findByRaceOrderByPlacementAsc(Race race);
	List<PilotRace> findByPlacementBetweenAndRace(Integer placementIn, Integer placementFin, Race race);
	PilotRace findByPilotAndRace(Pilot pilot, Race race);
	
}