package br.com.welitoncardozo.formula1.services;

import br.com.welitoncardozo.formula1.models.Country;
import br.com.welitoncardozo.formula1.services.exceptions.ObjectNotFound;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class CountryServiceTest extends BaseTest {
    @Inject
    private CountryService countryService;

    @Test
    void testSave() {
        final var country = new Country(null, "Estados Unidos");
        assertThat(countryService.salvar(country))
                .extracting(Country::getName)
                .isEqualTo(country.getName());
    }

    @Test
    @Sql({"classpath:sql/country.sql"})
    void testUpdate() {
        final var country = new Country(1, "Brasil alterado");
        assertThat(countryService.update(country))
                .extracting(Country::getId, Country::getName)
                .containsExactly(country.getId(), country.getName());
    }

    @Test
    @Sql({"classpath:sql/country.sql"})
    void testDelete() {
        final var id = 1;
        assertThat(countryService.findById(id)).isNotNull();
        countryService.delete(id);
        assertThatThrownBy(() -> countryService.findById(id))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage("País não existe");
    }

    @Test
    @Sql({"classpath:sql/country.sql"})
    void testFindById() {
        assertThat(countryService.findById(1)).isNotNull();
        assertThatThrownBy(() -> countryService.findById(5))
                .isInstanceOf(ObjectNotFound.class)
                .hasMessage("País não existe");
    }

    @Test
    @Sql({"classpath:sql/country.sql"})
    void testFindAll() {
        assertThat(countryService.listAll())
                .isNotEmpty()
                .hasSize(3)
                .extracting(Country::getId, Country::getName)
                .containsExactlyInAnyOrder(
                        tuple(1, "Brasil"),
                        tuple(2, "Argentina"),
                        tuple(3, "Inglaterra")
                );
    }

    @Test
    @Sql({"classpath:sql/country.sql"})
    void testFindWithFilter() {
        assertThat(countryService.findByNomeEqualsIgnoreCase("brasil"))
                .isNotEmpty()
                .hasSize(1)
                .extracting(Country::getId, Country::getName)
                .contains(tuple(1, "Brasil"));
        assertThat(countryService.findByNomeEqualsIgnoreCase("brazil")).isEmpty();

        assertThat(countryService.findByNomeEqualsIgnoreCase("argentina"))
                .isNotEmpty()
                .hasSize(1)
                .extracting(Country::getId, Country::getName)
                .contains(tuple(2, "Argentina"));
        assertThat(countryService.findByNomeEqualsIgnoreCase("argentin")).isEmpty();

        assertThat(countryService.findByNomeEqualsIgnoreCase("inglaterra"))
                .isNotEmpty()
                .hasSize(1)
                .extracting(Country::getId, Country::getName)
                .contains(tuple(3, "Inglaterra"));
        assertThat(countryService.findByNomeEqualsIgnoreCase("inglaterraa")).isEmpty();
    }
}
