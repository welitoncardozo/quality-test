package br.com.welitoncardozo.formula1.services;

import br.com.welitoncardozo.formula1.models.Country;
import br.com.welitoncardozo.formula1.models.Pilot;
import br.com.welitoncardozo.formula1.models.Team;
import br.com.welitoncardozo.formula1.services.exceptions.ObjectNotFound;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
@Sql({"classpath:sql/pilot.sql"})
class PilotServiceTest extends BaseTest {
    private static final Country COUNTRY = new Country(1, null);
    private static final Team TEAM = new Team(1, null);

    @Inject
    private PilotService pilotService;

    @Test
    void testSave() {
        final var pilot = new Pilot(null, "Novo piloto", COUNTRY, TEAM);
        assertThat(pilotService.insert(pilot))
                .extracting(Pilot::getName)
                .isEqualTo(pilot.getName());
    }

    @Test
    void testUpdate() {
        final var pilot = new Pilot(10, "Piloto alterado", COUNTRY, TEAM);
        assertThat(pilotService.update(pilot))
                .extracting(Pilot::getName)
                .isEqualTo(pilot.getName());

        final var idNotFound = 20;
        final var pilotNotFound = new Pilot(idNotFound, "Piloto inexistente", COUNTRY, TEAM);
        assertThatThrownBy(() -> pilotService.update(pilotNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Pilot %s não existe".formatted(idNotFound));
    }

    @Test
    void testDelete() {
        final var id = 10;
        assertThat(pilotService.findById(id)).isNotNull();
        pilotService.delete(id);
        assertThatThrownBy(() -> pilotService.findById(id))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage("Pilot %s não existe".formatted(id));
    }

    @Test
    void testFindById() {
        assertThat(pilotService.findById(10)).isNotNull();
        final var idNotFound = 20;
        assertThatThrownBy(() -> pilotService.findById(idNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Pilot %s não existe".formatted(idNotFound));
    }

    @Test
    void testFindAll() {
        assertThat(pilotService.listAll())
                .isNotEmpty()
                .hasSize(1)
                .extracting(Pilot::getId, Pilot::getName)
                .containsExactlyInAnyOrder(tuple(10, "Piloto principal"));
    }

    @Test
    void testFindWithFilter() {
        assertThat(pilotService.findByNameStartsWithIgnoreCase("piloto"))
                .isNotEmpty()
                .hasSize(1)
                .extracting(Pilot::getId, Pilot::getName)
                .containsExactlyInAnyOrder(tuple(10, "Piloto principal"));
        assertThatThrownBy(() -> pilotService.findByNameStartsWithIgnoreCase("Não existe")).
                isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum piloto com esse nome");

        assertThat(pilotService.findByCountry(COUNTRY))
                .isNotEmpty()
                .hasSize(1)
                .extracting(Pilot::getId, Pilot::getName)
                .containsExactlyInAnyOrder(tuple(10, "Piloto principal"));
        assertThatThrownBy(() -> pilotService.findByCountry(new Country(2, null))).
                isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum piloto nesse país");

        assertThat(pilotService.findByTeam(TEAM))
                .isNotEmpty()
                .hasSize(1)
                .extracting(Pilot::getId, Pilot::getName)
                .containsExactlyInAnyOrder(tuple(10, "Piloto principal"));
        assertThatThrownBy(() -> pilotService.findByTeam(new Team(2, null))).
                isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhum piloto nesse time");
    }
}
