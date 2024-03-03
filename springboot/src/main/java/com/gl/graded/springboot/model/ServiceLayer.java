package com.gl.graded.springboot.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;



@Service
public class ServiceLayer {

	//Autowired the Jpa repository
	@Autowired
	ServiceRepository repo;
	
	
	//Function for Add
	public void add(Ticket ticket) {
        repo.save(ticket);
    }
    
   
    //Function for get all Records
    public List<Ticket> getAll() {
        return repo.findAll();
    }
    
    //function for delete
    public void delete(Ticket ticket) {
        repo.delete(ticket);
    }
    
    
    
    //function for get records by id
    public Ticket getTicketById(int id) {
        return repo.findById(id).orElse(null);
    }
    
    
    // Method to update a ticket in the database
    public Ticket updateTicket(Ticket updatedTicket) {
        Ticket existingTicket = repo.findById(updatedTicket.getId())
                                                 .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        // Update the fields 
        existingTicket.setTitle(updatedTicket.getTitle());
        existingTicket.setDescription(updatedTicket.getDescription());
        existingTicket.setContent(updatedTicket.getContent());
        // Save the updated ticket back to the database
        return repo.save(existingTicket);
    }
    
    
    //Method for search operation
    public List<Ticket> filterByTitle(String searchKey){
		Ticket dummyTicket=new Ticket();
		dummyTicket.setTitle(searchKey);
		
		ExampleMatcher  em=ExampleMatcher.matching().withMatcher("title",ExampleMatcher.GenericPropertyMatchers.contains()).withIgnorePaths("id","description","date","content");
		
		Example<Ticket> example=Example.of(dummyTicket,em);
		
		return repo.findAll(example);
	}

}
