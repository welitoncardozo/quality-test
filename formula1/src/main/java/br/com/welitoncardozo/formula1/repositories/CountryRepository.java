package br.com.welitoncardozo.formula1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.welitoncardozo.formula1.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{
	List<Country> findByNameEqualsIgnoreCase(String nome);
}
