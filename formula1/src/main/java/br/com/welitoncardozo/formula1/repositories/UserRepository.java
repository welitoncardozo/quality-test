package br.com.welitoncardozo.formula1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.welitoncardozo.formula1.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	List<User> findByNameStartingWithIgnoreCase(String name);
	Optional<User> findByEmail(String email);
	Optional<User> findByName(String name);

}
