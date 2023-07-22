package br.com.welitoncardozo.formula1.repositories;

import java.util.List;

import br.com.welitoncardozo.formula1.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
	boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);
	boolean existsByNameIgnoreCase(String name);
	List<Team> findByNameIgnoreCase(String name);
	List<Team> findByNameContains(String name);
}
