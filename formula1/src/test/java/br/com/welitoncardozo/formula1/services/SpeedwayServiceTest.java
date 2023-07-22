package br.com.welitoncardozo.formula1.services;

import br.com.welitoncardozo.formula1.models.Country;
import br.com.welitoncardozo.formula1.models.Speedway;
import br.com.welitoncardozo.formula1.services.exceptions.IntegrityViolation;
import br.com.welitoncardozo.formula1.services.exceptions.ObjectNotFound;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
@Sql({"classpath:sql/speedway.sql"})
class SpeedwayServiceTest extends BaseTest {
    private static final Country COUNTRY = new Country(1, null);

    @Inject
    private SpeedwayService speedwayService;

    @Test
    void testSave() {
        final var speedway = new Speedway(null, "Nova pista", 1000, COUNTRY);
        assertThat(speedwayService.insert(speedway))
                .extracting(Speedway::getName, Speedway::getSize, Speedway::getCountry)
                .contains(speedway.getName(), speedway.getSize(), speedway.getCountry());
    }

    @Test
    void testSaveSizeInvalid() {
        final var speedway = new Speedway(null, "Nova pista", null, COUNTRY);
        assertThatThrownBy(() -> speedwayService.insert(speedway))
                .isInstanceOf(IntegrityViolation.class)
                .hasMessage("Tamanho da pista inválido");
    }

    @Test
    void testUpdate() {
        final var speedway = new Speedway(10, "Pista alterada", 2000, COUNTRY);
        assertThat(speedwayService.update(speedway))
                .extracting(Speedway::getName, Speedway::getSize, Speedway::getCountry)
                .contains(speedway.getName(), speedway.getSize(), speedway.getCountry());

        final var idNotFound = 20;
        final var speedwayNotFound = new Speedway(idNotFound, "Piloto inexistente", null, COUNTRY);
        assertThatThrownBy(() -> speedwayService.update(speedwayNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Pista %s não existe".formatted(idNotFound));

        final var speedwayInvalidSize = new Speedway(10, "Tamanho inválido", null, COUNTRY);
        assertThatThrownBy(() -> speedwayService.update(speedwayInvalidSize))
                .isInstanceOf(IntegrityViolation.class)
                .hasMessage("Tamanho da pista inválido");
    }

    @Test
    void testDelete() {
        final var id = 10;
        assertThat(speedwayService.findById(id)).isNotNull();
        speedwayService.delete(id);
        assertThatThrownBy(() -> speedwayService.findById(id))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage("Pista %s não existe".formatted(id));
    }

    @Test
    void testFindById() {
        assertThat(speedwayService.findById(10)).isNotNull();
        final var idNotFound = 20;
        assertThatThrownBy(() -> speedwayService.findById(idNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Pista %s não existe".formatted(idNotFound));
    }

    @Test
    void testFindAll() {
        assertThat(speedwayService.listAll())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void testFindWithFilter() {
        assertThat(speedwayService.findByNameStartsWithIgnoreCase("interlagos"))
                .isNotEmpty()
                .hasSize(1)
                .extracting(Speedway::getId, Speedway::getName, Speedway::getSize, Speedway::getCountry)
                .containsExactlyInAnyOrder(tuple(10, "Interlagos", 50000, COUNTRY));
        assertThatThrownBy(() -> speedwayService.findByNameStartsWithIgnoreCase("Não existe")).
                isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhuma pista cadastrada com esse nome");

        assertThat(speedwayService.findBySizeBetween(10, 800000))
                .isNotEmpty()
                .hasSize(1)
                .extracting(Speedway::getId, Speedway::getName, Speedway::getSize, Speedway::getCountry)
                .containsExactlyInAnyOrder(tuple(10, "Interlagos", 50000, COUNTRY));
        assertThatThrownBy(() -> speedwayService.findBySizeBetween(0, 500))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhuma pista cadastrada com essas medidas");

        assertThat(speedwayService.findByCountryOrderBySizeDesc(COUNTRY))
                .isNotEmpty()
                .hasSize(1)
                .extracting(Speedway::getId, Speedway::getName, Speedway::getSize, Speedway::getCountry)
                .containsExactlyInAnyOrder(tuple(10, "Interlagos", 50000, COUNTRY));
        final var countryNotFound = new Country(50, "País inexistente");
        assertThatThrownBy(() -> speedwayService.findByCountryOrderBySizeDesc(countryNotFound))
                .isExactlyInstanceOf(ObjectNotFound.class)
                .hasMessage("Nenhuma pista cadastrada no país: %s".formatted(countryNotFound.getName()));
    }
}
