package br.com.welitoncardozo.formula1.services;

import java.util.List;

import br.com.welitoncardozo.formula1.models.Pilot;
import br.com.welitoncardozo.formula1.models.PilotRace;
import br.com.welitoncardozo.formula1.models.Race;

public interface PilotRaceService {

	PilotRace findById(Integer id);

	PilotRace insert(PilotRace pilotRace);

	List<PilotRace> listAll();

	PilotRace update(PilotRace pilotRace);

	void delete(Integer id);

	List<PilotRace> findByPlacement(Integer placement);

	List<PilotRace> findByPilot(Pilot pilot);

	List<PilotRace> findByRaceOrderByPlacementAsc(Race race);

	List<PilotRace> findByPlacementBetweenAndRace(Integer placementIn, Integer placementFin, Race race);

	PilotRace findByPilotAndRace(Pilot pilot, Race race);

}
