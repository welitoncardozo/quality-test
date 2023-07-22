package br.com.welitoncardozo.formula1.services;

import java.util.List;

import br.com.welitoncardozo.formula1.models.Country;

public interface CountryService {

	Country salvar(Country country);

	Country update(Country country);

	void delete(Integer id);

	List<Country> listAll();

	Country findById(Integer id);

	List<Country> findByNomeEqualsIgnoreCase(String nome);

}
