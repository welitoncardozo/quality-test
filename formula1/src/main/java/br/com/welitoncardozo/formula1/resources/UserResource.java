package br.com.welitoncardozo.formula1.resources;

import java.util.List;

import br.com.welitoncardozo.formula1.models.dto.UserDTO;
import br.com.welitoncardozo.formula1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.welitoncardozo.formula1.models.User;

@RestController
@RequestMapping("/users")
public class UserResource {
	
	@Autowired
	private UserService service;
	
//	@Secured({"ROLE_ADMIN"})
	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody UserDTO user) {
		return ResponseEntity.ok(service.insert(new User(user)).toDTO());
	}
	
//	@Secured({"ROLE_USER"})
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Integer id){
		return ResponseEntity.ok(service.findById(id).toDTO());
	}
	
//	@Secured({"ROLE_USER"})
	@GetMapping("/name/{name}")
	public ResponseEntity<List<UserDTO>> findByName(@PathVariable String name){
		return ResponseEntity.ok(service.findByName(name).stream()
				.map(User::toDTO)
				.toList());
	}
	
//	@Secured({"ROLE_USER"})
	@GetMapping
	public ResponseEntity<List<UserDTO>> listAll(){
		return ResponseEntity.ok(service.listAll().stream()
				.map((user) -> user.toDTO())
				.toList());
	}
	
	@Secured({"ROLE_ADMIN"})
	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Integer id, @RequestBody UserDTO userDTO){
		User user = new User(userDTO);
		user.setId(id);
		return ResponseEntity.ok(service.update(user).toDTO());
	}
	
//	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	

}
