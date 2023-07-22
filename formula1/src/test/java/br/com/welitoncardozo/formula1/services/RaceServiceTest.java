package br.com.welitoncardozo.formula1.services;

import br.com.welitoncardozo.formula1.models.Championship;
import br.com.welitoncardozo.formula1.models.Country;
import br.com.welitoncardozo.formula1.models.Race;
import br.com.welitoncardozo.formula1.models.Speedway;
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
@Sql({"classpath:sql/race.sql"})
class RaceServiceTest extends BaseTest {
    private static final Country COUNTRY = new Country(1, null);
    private static final Speedway SPEEDWAY = new Speedway(1, null, null, COUNTRY);
    private static final Championship CHAMPIONSHIP = new Championship(1, null, null);

    @Inject
    private RaceService raceService;

    @Test
    void testSave() {
        final var race = new Race(null, ZonedDateTime.now(), SPEEDWAY, CHAMPIONSHIP);
        assertThat(raceService.insert(race))
                .extracting(Race::getDate, Race::getSpeedway, Race::getChampionship)
                .containsExactly(race.getDate(), race.getSpeedway(), race.getChampionship());
    }

    @Test
    void testSaveDateInvalid() {
        final var race = new Race(null, null, SPEEDWAY, CHAMPIONSHIP);
        assertThatThrownBy(() -> raceService.insert(race))
                .isInstanceOf(IntegrityViolation.class)
                .hasMessage("Data inválida");
    }

    @Test
    void testSaveYearInvalid() {
        final var race = new Race(null, ZonedDateTime.now().minusYears(10), SPEEDWAY, CHAMPIONSHIP);
        assertThatThrownBy(() -> raceService.insert(race))
                .isInstanceOf(IntegrityViolation.class)
                .hasMessage("Ano da corrida diferente do ano do campeonato");
    }

    @Test
    void testUpdate() {
        final var race = new Race(10, ZonedDateTime.now().plusHours(5), SPEEDWAY, CHAMPIONSHIP);
        assertThat(raceService.update(race))
                .extracting(Race::getDate)
                .isEqualTo(race.getDate());

        final var idNotFound = 20;
        final var raceNotFound = new Race(idNotFound, ZonedDateTime.now().plusHours(5), SPEEDWAY, CHAMPIONSHIP);
        assertThatThrownBy(() -> raceService.update(raceNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Corrida %s não existe".formatted(idNotFound));
    }

    @Test
    void testDelete() {
        final var id = 10;
        assertThat(raceService.findById(id)).isNotNull();
        raceService.delete(id);
        assertThatThrownBy(() -> raceService.findById(id))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage("Corrida %s não existe".formatted(id));
    }

    @Test
    void testFindById() {
        assertThat(raceService.findById(10)).isNotNull();
        final var idNotFound = 20;
        assertThatThrownBy(() -> raceService.findById(idNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Corrida %s não existe".formatted(idNotFound));
    }

    @Test
    void testFindAll() {
        assertThat(raceService.listAll())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void testFindByDate() {
        final var currentDate = ZonedDateTime.now();
        assertThat(raceService.findByDate(currentDate))
                .isNotEmpty()
                .hasSize(1)
                .extracting(
                        Race::getId,
                        it -> it.getDate().getYear(),
                        it -> it.getDate().getMonthValue(),
                        Race::getSpeedway,
                        Race::getChampionship
                )
                .containsExactlyInAnyOrder(tuple(
                        10,
                        currentDate.getYear(),
                        currentDate.getMonthValue(),
                        SPEEDWAY,
                        CHAMPIONSHIP
                ));
        assertThatThrownBy(() -> raceService.findByDate(currentDate.minusYears(10)))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Não existe corrida para o ano especificado");
    }

    @Test
    void testFindBySpeedway() {
        final var currentDate = ZonedDateTime.now();
        assertThat(raceService.findBySpeedway(SPEEDWAY))
                .isNotEmpty()
                .hasSize(1)
                .extracting(
                        Race::getId,
                        it -> it.getDate().getYear(),
                        it -> it.getDate().getMonthValue(),
                        Race::getSpeedway,
                        Race::getChampionship
                )
                .containsExactlyInAnyOrder(tuple(
                        10,
                        currentDate.getYear(),
                        currentDate.getMonthValue(),
                        SPEEDWAY,
                        CHAMPIONSHIP
                ));
        assertThatThrownBy(() -> raceService.findBySpeedway(new Speedway(2, null, null, null)))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Não existe corrida na pista especificada");
    }

    @Test
    void testFindByChampionship() {
        final var currentDate = ZonedDateTime.now();
        assertThat(raceService.findByChampionship(CHAMPIONSHIP))
                .isNotEmpty()
                .hasSize(1)
                .extracting(
                        Race::getId,
                        it -> it.getDate().getYear(),
                        it -> it.getDate().getMonthValue(),
                        Race::getSpeedway,
                        Race::getChampionship
                )
                .containsExactlyInAnyOrder(tuple(
                        10,
                        currentDate.getYear(),
                        currentDate.getMonthValue(),
                        SPEEDWAY,
                        CHAMPIONSHIP
                ));
        assertThatThrownBy(() -> raceService.findByChampionship(new Championship(2, null, null)))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Não existe corrida para o campeonato especificado");
    }
}
