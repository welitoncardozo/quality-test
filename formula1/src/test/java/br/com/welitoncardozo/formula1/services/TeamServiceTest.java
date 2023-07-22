package br.com.welitoncardozo.formula1.services;

import br.com.welitoncardozo.formula1.models.Team;
import br.com.welitoncardozo.formula1.services.exceptions.IntegrityViolation;
import br.com.welitoncardozo.formula1.services.exceptions.ObjectNotFound;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class TeamServiceTest extends BaseTest {
    private static final String TEAM_NAME_ALREADY_EXISTS = "Team";

    @Inject
    private TeamService teamService;

    @Test
    void testSaveSuccess() {
        final var team = new Team(null, "Ferrari");
        assertThat(teamService.salvar(team))
                .extracting(Team::getName)
                .isEqualTo(team.getName());
    }

    @Test
    @Sql({"classpath:sql/team.sql"})
    void testSaveNameAlreadyExists() {
        final var team = new Team(null, TEAM_NAME_ALREADY_EXISTS);
        assertThatThrownBy(() -> teamService.salvar(team))
                .isInstanceOf(IntegrityViolation.class)
                .hasMessage(String.format("Nome já existente: %s", team.getName()));
    }

    @Test
    @Sql({"classpath:sql/team.sql"})
    void testUpdate() {
        final var nameChanged = "Name changed";
        assertThat(teamService.update(new Team(1, nameChanged)))
                .extracting(Team::getName)
                .isEqualTo(nameChanged);
    }

    @Test
    @Sql({"classpath:sql/team.sql"})
    void testDelete() {
        final var idToDelete = 1;

        teamService.delete(idToDelete);
        assertThatThrownBy(() -> teamService.findById(idToDelete))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage(String.format("Equipe %s não encontrada", idToDelete));

        assertThatThrownBy(() -> teamService.delete(idToDelete))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage(String.format("Equipe %s não encontrada", idToDelete));
    }

    @Test
    @Sql({"classpath:sql/team.sql"})
    void testListTeam() {
        assertThat(teamService.listAll())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void testListEmptyTeam() {
        assertThatThrownBy(() -> teamService.listAll())
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage("Não existe equipes cadastradas");
    }

    @Test
    @Sql({"classpath:sql/team.sql"})
    void testFindById() {
        assertThat(teamService.findById(1))
                .isNotNull()
                .extracting(Team::getName)
                .isEqualTo(TEAM_NAME_ALREADY_EXISTS);

        final var idNotFound = 2;
        assertThatThrownBy(() -> teamService.findById(idNotFound))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage(String.format("Equipe %s não encontrada", idNotFound));
    }

    @Test
    @Sql({"classpath:sql/team.sql"})
    void testFindByNameIgnoreCase() {
        assertThat(teamService.findByNameIgnoreCase(TEAM_NAME_ALREADY_EXISTS))
                .isNotEmpty()
                .hasSize(1);

        final var teamNotRegistered = "Team not registered";
        assertThatThrownBy(() -> teamService.findByNameIgnoreCase(teamNotRegistered))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage(String.format("Equipe %s não encontrada", teamNotRegistered));
    }

    @Test
    @Sql({"classpath:sql/team.sql"})
    void testFindByNameContains() {
        assertThat(teamService.findByNameContains(TEAM_NAME_ALREADY_EXISTS.substring(0, 2)))
                .isNotEmpty()
                .hasSize(1);

        final var teamNotRegistered = "Not registered";
        assertThatThrownBy(() -> teamService.findByNameContains(teamNotRegistered))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage(String.format("Nome %s não encontrado em nenhuma equipe", teamNotRegistered));
    }
}
