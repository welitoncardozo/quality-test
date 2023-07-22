package br.com.welitoncardozo.formula1.services.impl;

import java.util.List;
import java.util.Optional;

import br.com.welitoncardozo.formula1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.welitoncardozo.formula1.models.User;
import br.com.welitoncardozo.formula1.repositories.UserRepository;
import br.com.welitoncardozo.formula1.services.exceptions.IntegrityViolation;
import br.com.welitoncardozo.formula1.services.exceptions.ObjectNotFound;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository repository;
	
	private void findByEmail(User user) {
		User busca = repository.findByEmail(user.getEmail()).orElse(null);
		if(busca != null && !busca.getId().equals(user.getId())) {
			throw new IntegrityViolation("Email já existente: %s"
					.formatted(user.getEmail()));
		}
	}

	@Override
	public User findById(Integer id) {
		Optional<User> user = repository.findById(id);
		return user.orElseThrow(()-> 
		new ObjectNotFound("O usuário %s não existe".formatted(id)));
	}

	@Override
	public User insert(User user) {
		findByEmail(user);
		return repository.save(user);
	}

	@Override
	public List<User> listAll() {
		List<User> lista = repository.findAll();
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum usuário cadastrado");
		}
		return lista;
	}

	@Override
	public User update(User user) {
		findById(user.getId());
		findByEmail(user);
		return repository.save(user);
	}

	@Override
	public void delete(Integer id) {
		User user = findById(id);
		repository.delete(user);
	}

	@Override
	public List<User> findByName(String name) {
		List<User> lista = repository.findByNameStartingWithIgnoreCase(name);
		if(lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum nome de usuário inicia com %s".formatted(name));
		}
		return lista;
	}

}
