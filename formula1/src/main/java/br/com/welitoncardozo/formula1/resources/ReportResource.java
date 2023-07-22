package br.com.welitoncardozo.formula1.resources;

import java.util.List;
import java.util.stream.Stream;

import br.com.welitoncardozo.formula1.models.dto.RaceCountryYearDTO;
import br.com.welitoncardozo.formula1.models.dto.RaceDTO;
import br.com.welitoncardozo.formula1.services.CountryService;
import br.com.welitoncardozo.formula1.services.RaceService;
import br.com.welitoncardozo.formula1.services.SpeedwayService;
import br.com.welitoncardozo.formula1.services.exceptions.ObjectNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.welitoncardozo.formula1.models.Country;
import br.com.welitoncardozo.formula1.models.Race;

@RestController
@RequestMapping("/reports")
public class ReportResource {
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private SpeedwayService speedwayService;
	
	@Autowired
	private RaceService raceService;
	
	
	@GetMapping("/races-by-country-year/{countryId}/{year}")
	public ResponseEntity<RaceCountryYearDTO> findRaceByCountryAndYear(@PathVariable Integer countryId, @PathVariable Integer year){
		
		Country country = countryService.findById(countryId);
		
		List<RaceDTO> raceDTOs = speedwayService.findByCountryOrderBySizeDesc(country).stream()
		        .flatMap(speedway -> {
		            try {
		                return raceService.findBySpeedway(speedway).stream();
		            } catch (ObjectNotFound e) {
		                return Stream.empty();
		            }
		        })
		        .filter(race -> race.getDate().getYear() == year)
		        .map(Race::toDTO)
		        .toList();
		
				
		return ResponseEntity.ok(new RaceCountryYearDTO(year, country.getName(), raceDTOs.size(), raceDTOs));
		
		
	}

}
