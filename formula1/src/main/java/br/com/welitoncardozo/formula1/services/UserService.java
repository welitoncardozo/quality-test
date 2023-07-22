package br.com.welitoncardozo.formula1.services;

import java.util.List;

import br.com.welitoncardozo.formula1.models.User;

public interface UserService {

	User findById(Integer id);

	User insert(User user);

	List<User> listAll();
	
	User update(User user);
	
	void delete(Integer id);
	
	List<User> findByName(String name);

}
