package br.com.welitoncardozo.formula1.services;

import br.com.welitoncardozo.formula1.models.Championship;
import br.com.welitoncardozo.formula1.models.Country;
import br.com.welitoncardozo.formula1.models.Pilot;
import br.com.welitoncardozo.formula1.models.PilotRace;
import br.com.welitoncardozo.formula1.models.Race;
import br.com.welitoncardozo.formula1.models.Speedway;
import br.com.welitoncardozo.formula1.models.Team;
import br.com.welitoncardozo.formula1.services.exceptions.IntegrityViolation;
import br.com.welitoncardozo.formula1.services.exceptions.ObjectNotFound;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
@Sql({"classpath:sql/pilot_race.sql"})
class PilotRaceServiceTest extends BaseTest {
    private static final Country COUNTRY = new Country(1, null);
    private static final Team TEAM = new Team(1, null);
    private static final Pilot PILOT = new Pilot(1, null, COUNTRY, TEAM);
    private static final Speedway SPEEDWAY = new Speedway(1, null, null, COUNTRY);
    private static final Championship CHAMPIONSHIP = new Championship(1, null, null);
    private static final Race RACE = new Race(1, ZonedDateTime.now(), SPEEDWAY, CHAMPIONSHIP);

    @Inject
    private PilotRaceService pilotRaceService;

    @Test
    void testSave() {
        final var pilotRace = new PilotRace(null, 1, PILOT, RACE);
        assertThat(pilotRaceService.insert(pilotRace))
                .extracting(PilotRace::getPlacement, PilotRace::getPilot, PilotRace::getRace)
                .contains(pilotRace.getPlacement(), pilotRace.getPilot(), pilotRace.getRace());
    }

    @Test
    void testSavePlacementNull() {
        final var pilotRace = new PilotRace(null, null, PILOT, RACE);
        assertThatThrownBy(() -> pilotRaceService.insert(pilotRace))
                .isExactlyInstanceOf(IntegrityViolation.class)
                .hasMessage("Colocacao null!");
    }

    @Test
    void testSavePlacementZero() {
        final var pilotRace = new PilotRace(null, 0, PILOT, RACE);
        assertThatThrownBy(() -> pilotRaceService.insert(pilotRace))
                .isExactlyInstanceOf(IntegrityViolation.class)
                .hasMessage("Colocacao zero!");
    }

    @Test
    void testUpdate() {
        final var pilotRace = new PilotRace(10, 3, PILOT, RACE);
        assertThat(pilotRaceService.update(pilotRace))
                .extracting(PilotRace::getPlacement, PilotRace::getPilot, PilotRace::getRace)
                .contains(pilotRace.getPlacement(), pilotRace.getPilot(), pilotRace.getRace());
    }

    @Test
    void testUpdateNotFound() {
        final var idNotFound = 20;
        final var pilotRaceNotFound = new PilotRace(idNotFound, 3, PILOT, RACE);
        assertThatThrownBy(() -> pilotRaceService.update(pilotRaceNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("ID %s inválido!".formatted(idNotFound));
    }

    @Test
    void testUpdatePlacementNull() {
        final var pilotRace = new PilotRace(10, null, PILOT, RACE);
        assertThatThrownBy(() -> pilotRaceService.update(pilotRace))
                .isExactlyInstanceOf(IntegrityViolation.class)
                .hasMessage("Colocacao null!");
    }

    @Test
    void testUpdatePlacementZero() {
        final var pilotRace = new PilotRace(10, 0, PILOT, RACE);
        assertThatThrownBy(() -> pilotRaceService.update(pilotRace))
                .isExactlyInstanceOf(IntegrityViolation.class)
                .hasMessage("Colocacao zero!");
    }

    @Test
    void testDelete() {
        final var id = 10;
        assertThat(pilotRaceService.findById(id)).isNotNull();
        pilotRaceService.delete(id);
        assertThatThrownBy(() -> pilotRaceService.findById(id))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage("ID %s inválido!".formatted(id));
    }

    @Test
    void testFindById() {
        assertThat(pilotRaceService.findById(10)).isNotNull();
        final var idNotFound = 20;
        assertThatThrownBy(() -> pilotRaceService.findById(idNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("ID %s inválido!".formatted(idNotFound));
    }

    @Test
    void testFindAll() {
        assertThat(pilotRaceService.listAll())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void testFindByPlacement() {
        assertThat(pilotRaceService.findByPlacement(1))
                .isNotEmpty()
                .hasSize(1)
                .extracting(PilotRace::getPlacement, PilotRace::getPilot, PilotRace::getRace)
                .containsExactlyInAnyOrder(tuple(1, PILOT, RACE));
        assertThatThrownBy(() -> pilotRaceService.findByPlacement(2))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum PilotoCorrida nesta posição!");
    }

    @Test
    void testFindByPilot() {
        assertThat(pilotRaceService.findByPilot(PILOT))
                .isNotEmpty()
                .hasSize(1)
                .extracting(PilotRace::getPlacement, PilotRace::getPilot, PilotRace::getRace)
                .containsExactlyInAnyOrder(tuple(1, PILOT, RACE));
        assertThatThrownBy(() -> pilotRaceService.findByPilot(new Pilot(2, null, COUNTRY, TEAM)))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum PilotoCorrida com esse piloto!");
    }

    @Test
    void testFindByRaceOrderByPlacementAsc() {
        assertThat(pilotRaceService.findByRaceOrderByPlacementAsc(RACE))
                .isNotEmpty()
                .hasSize(1)
                .extracting(PilotRace::getPlacement, PilotRace::getPilot, PilotRace::getRace)
                .containsExactlyInAnyOrder(tuple(1, PILOT, RACE));
        assertThatThrownBy(() -> pilotRaceService.findByRaceOrderByPlacementAsc(new Race(2, null, SPEEDWAY, CHAMPIONSHIP)))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum PilotoCorrida nesta corrida!");
    }

    @Test
    void testFindByPlacementBetweenAndRace() {
        assertThat(pilotRaceService.findByPlacementBetweenAndRace(1, 10, RACE))
                .isNotEmpty()
                .hasSize(1)
                .extracting(PilotRace::getPlacement, PilotRace::getPilot, PilotRace::getRace)
                .containsExactlyInAnyOrder(tuple(1, PILOT, RACE));
        assertThatThrownBy(() -> pilotRaceService.findByPlacementBetweenAndRace(20, 50, RACE))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum PilotoCorrida com esses parâmetros de busca!");
    }

    @Test
    void testFindByPilotAndRace() {
        assertThat(pilotRaceService.findByPilotAndRace(PILOT, RACE))
                .isNotNull()
                .extracting(PilotRace::getPlacement, PilotRace::getPilot, PilotRace::getRace)
                .contains(1, PILOT, RACE);
    }

    @Test
    void testFindByPilotAndRaceNotFound() {
        final var pilotNotFound = new Pilot(2, null, COUNTRY, TEAM);
        final var raceNotFound = new Race(2, null, SPEEDWAY, CHAMPIONSHIP);

        assertThatThrownBy(() -> pilotRaceService.findByPilotAndRace(pilotNotFound, RACE))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum PilotoCorrida com esses parâmetros de busca!");
        assertThatThrownBy(() -> pilotRaceService.findByPilotAndRace(PILOT, raceNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum PilotoCorrida com esses parâmetros de busca!");
        assertThatThrownBy(() -> pilotRaceService.findByPilotAndRace(pilotNotFound, raceNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum PilotoCorrida com esses parâmetros de busca!");
    }
}
