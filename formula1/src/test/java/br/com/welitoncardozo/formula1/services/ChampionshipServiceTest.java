package br.com.welitoncardozo.formula1.services;

import br.com.welitoncardozo.formula1.models.Championship;
import br.com.welitoncardozo.formula1.services.exceptions.IntegrityViolation;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class ChampionshipServiceTest extends BaseTest {
    @Inject
    private ChampionshipService championshipService;

    @Test
    void testInsertSuccess() {
        final var championship = new Championship(null, "Novo campeonato", 2022);
        assertThat(championshipService.insert(championship))
                .extracting(Championship::getDescription)
                .isEqualTo(championship.getDescription());
    }

    @Test
    void testInsertWithoutYear() {
        final var championship = new Championship(null, "Novo campeonato", null);
        assertThatThrownBy(() -> championshipService.insert(championship))
                .isInstanceOf(IntegrityViolation.class)
                .hasMessage("Ano não pode ser nulo");
    }

    @Test
    void testInsertYearInvalid() {
        final var championship = new Championship(null, "Novo campeonato", 1500);
        assertThatThrownBy(() -> championshipService.insert(championship))
                .isInstanceOf(IntegrityViolation.class)
                .hasMessage("Ano inválido: %s".formatted(championship.getYear()));
    }

    @Sql({"classpath:sql/championship.sql"})
    @Test
    void testUpdate() {
        final var championship = new Championship(1, "Campeonato alterado", 2016);
        assertThat(championshipService.update(championship))
                .isNotNull()
                .extracting(Championship::getDescription, Championship::getYear)
                .containsExactly(championship.getDescription(), championship.getYear());

        final var championshipInvalid = new Championship(1, "Novo campeonato", 1500);
        assertThatThrownBy(() -> championshipService.insert(championshipInvalid))
                .isInstanceOf(IntegrityViolation.class)
                .hasMessage("Ano inválido: %s".formatted(championshipInvalid.getYear()));
    }

    @Sql({"classpath:sql/championship.sql"})
    @Test
    void testDelete() {
        final var id = 1;
        assertThat(championshipService.findById(id)).isNotNull();
        championshipService.delete(id);
        assertThat(championshipService.findById(id)).isNull();
    }

    @Test
    @Sql({"classpath:sql/championship.sql"})
    void testFindById() {
        assertThat(championshipService.findById(1))
                .isNotNull()
                .extracting(Championship::getDescription, Championship::getYear)
                .containsExactly("Campeonato nacional", 2015);

        assertThat(championshipService.findById(2))
                .isNull();
    }

    @Test
    @Sql({"classpath:sql/championship.sql"})
    void testListAll() {
        assertThat(championshipService.listAll())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @Sql({"classpath:sql/championship.sql"})
    void testListWithFilter() {
        assertThat(championshipService.findByYearBetween(2015, 2022)).isNotEmpty().hasSize(1);
        assertThat(championshipService.findByYearBetween(2000, 2010)).isEmpty();

        assertThat(championshipService.findByYear(2015)).isNotEmpty().hasSize(1);
        assertThat(championshipService.findByYear(2000)).isEmpty();

        assertThat(championshipService.findByDescriptionContainsIgnoreCase("Campeonato")).isNotEmpty().hasSize(1);
        assertThat(championshipService.findByDescriptionContainsIgnoreCase("Não existe")).isEmpty();

        assertThat(championshipService.findByDescriptionContainsIgnoreCaseAndYearEquals("Campeonato", 2015)).isNotEmpty().hasSize(1);
        assertThat(championshipService.findByDescriptionContainsIgnoreCaseAndYearEquals("Campeonato", 2000)).isEmpty();
        assertThat(championshipService.findByDescriptionContainsIgnoreCaseAndYearEquals("Não existe", 2015)).isEmpty();
    }
}
