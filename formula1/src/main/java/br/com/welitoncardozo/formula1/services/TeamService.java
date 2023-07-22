package br.com.welitoncardozo.formula1.services;

import java.util.List;

import br.com.welitoncardozo.formula1.models.Team;

public interface TeamService {

	Team salvar(Team team);

	List<Team> listAll();

	Team findById(Integer id);

	Team update(Team team);

	void delete(Integer id);

	List<Team> findByNameIgnoreCase(String name);

	List<Team> findByNameContains(String name);

}
