package br.com.welitoncardozo.formula1.services;

import java.util.List;

import br.com.welitoncardozo.formula1.models.Championship;

public interface ChampionshipService {

	Championship findById(Integer id);

	Championship insert(Championship championship);

	List<Championship> listAll();

	Championship update(Championship championship);

	void delete(Integer id);

	List<Championship> findByYearBetween(Integer start, Integer end);

	List<Championship> findByYear(Integer year);

	List<Championship> findByDescriptionContainsIgnoreCase(String descricao);

	List<Championship> findByescriptionContainsIgnoreCaseAndAnoEquals(String descricao, Integer ano);
}
