package org.generation.italy.api;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.generation.italy.model.Todo;
import org.generation.italy.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/todo")
public class TodoController {

	@Autowired
	private TodoRepository repo;
	

	@GetMapping
	public ResponseEntity<List<Todo>> list(){
		return new ResponseEntity<List<Todo>>(repo.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Todo> getById(@PathVariable Integer id){
		Todo todo = null;
		try{
			todo = repo.findById(id).get();
			return new ResponseEntity<Todo>(todo, HttpStatus.OK);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<Todo>(todo, HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PostMapping
	public ResponseEntity<Todo> create(@Valid @RequestBody Todo todo) {
		Todo newTodo = null;
		try {
			newTodo = repo.save(todo);
			return new ResponseEntity<Todo>(newTodo, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<Todo>(newTodo, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Todo> update(@PathVariable Integer id, @Valid @RequestBody Todo updatedTodo) {
		Todo todo = null;
		try{
			repo.findById(id).get();
			todo = repo.save(updatedTodo);
			return new ResponseEntity<Todo>(todo, HttpStatus.OK);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<Todo>(todo, HttpStatus.NOT_FOUND);
		} catch(Exception ex) {
			return new ResponseEntity<Todo>(todo, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/{id}/toggle")
	public ResponseEntity<Todo> toggle(@PathVariable Integer id) {
		Todo todo = null;
		try{
			todo = repo.findById(id).get();
			todo.setCompleted(!todo.getCompleted());
			repo.save(todo);
			return new ResponseEntity<Todo>(todo, HttpStatus.OK);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<Todo>(todo, HttpStatus.NOT_FOUND);
		} catch(Exception ex) {
			return new ResponseEntity<Todo>(todo, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		try{
			repo.findById(id).get();
			repo.deleteById(id);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<String>("Todo not found", HttpStatus.NOT_FOUND);
		}
	}
}
