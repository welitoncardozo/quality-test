package br.com.welitoncardozo.formula1.resources;

import java.util.List;

import br.com.welitoncardozo.formula1.models.dto.RaceDTO;
import br.com.welitoncardozo.formula1.services.ChampionshipService;
import br.com.welitoncardozo.formula1.services.RaceService;
import br.com.welitoncardozo.formula1.services.SpeedwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.welitoncardozo.formula1.models.Championship;
import br.com.welitoncardozo.formula1.models.Race;
import br.com.welitoncardozo.formula1.models.Speedway;
import br.com.welitoncardozo.formula1.utils.DateUtils;

@RestController
@RequestMapping("/racers")
public class RaceResource {
	
	@Autowired
	private RaceService service;

	@Autowired
	private SpeedwayService speedwayService;
	
	@Autowired
	private ChampionshipService championshipService;

	@GetMapping("/{id}")
	public ResponseEntity<RaceDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());
	}

	@PostMapping
	ResponseEntity<RaceDTO> insert(@RequestBody RaceDTO raceDTO) {
		return ResponseEntity.ok(service.insert(new Race(
				raceDTO, 
				championshipService.findById(raceDTO.getChampionshipId()), 
				speedwayService.findById(raceDTO.getSpeedwayId())))
			.toDTO());
	}

	@GetMapping
	ResponseEntity<List<RaceDTO>> listAll() {
		return ResponseEntity.ok(service.listAll()
				.stream()
				.map((race) -> race.toDTO())
				.toList());
	}

	@PutMapping("/{id}")
	ResponseEntity<RaceDTO> update(@PathVariable Integer id, @RequestBody RaceDTO raceDTO) {
		Speedway speedway = speedwayService.findById(raceDTO.getSpeedwayId());
		Championship championship = championshipService.findById(raceDTO.getChampionshipId());
		Race race = new Race(raceDTO, championship, speedway );
		race.setId(id);
		return ResponseEntity.ok(service.update(race).toDTO());
	}

	@DeleteMapping
	ResponseEntity<Void> delete(Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/date")
	ResponseEntity<List<RaceDTO>> findByDate(@RequestParam String date) {
		return ResponseEntity.ok(service.findByDate(DateUtils.strToZonedDateTime(date))
				.stream()
				.map(Race::toDTO)
				.toList());
	}

	@GetMapping("/speedway/{idSpeedway}")
	ResponseEntity<List<RaceDTO>> findBySpeedway(@PathVariable Integer idSpeedway) {
		return ResponseEntity.ok(service.findBySpeedway(speedwayService.findById(idSpeedway))
				.stream()
				.map(Race::toDTO)
				.toList());
	}
	
	@GetMapping("/championship/{idchampionship}")
	ResponseEntity<List<RaceDTO>> findByChampionship(@PathVariable Integer idchampionship) {
		return ResponseEntity.ok(service.findByChampionship(championshipService.findById(idchampionship))
				.stream()
				.map(Race::toDTO)
				.toList());
	}

}
