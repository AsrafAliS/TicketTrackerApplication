package com.gl.graded.springboot;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gl.graded.springboot.model.ServiceLayer;
import com.gl.graded.springboot.model.Ticket;



@Controller
public class TicketController {
    
    @Autowired
    ServiceLayer service;
    
    
    //Redirect To home page
    @RequestMapping("/home")
    public String home(Model data) {
    	
    	List<Ticket> tickets=service.getAll();
    	
    	data.addAttribute("ticket",tickets);
    	
        return "home";
    }
    
    //It will redirect you to ticket creation form
    @RequestMapping("/new-ticket")
    public String create() {
        return "createform";
    }
    //Insert function for ticket
    @PostMapping("/submit-ticket")
    public String insertRecord(@RequestParam String title,
                               @RequestParam String description,
                               @RequestParam String content,
                               RedirectAttributes redirect) {
        
        // Create a new Ticket object
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setContent(content);
        
        // Now, when this ticket object is saved using JPA, 
        // the date field will automatically be set to the current date before persisting
        
        service.add(ticket);
        
        // Redirect the user to the home page
        return "redirect:/home";
    }
    
    
    //Update Function that will redirect you to update form with that particular id record
    @GetMapping("/update")
    public String showUpdateForm(@RequestParam int id, Model model) {
        
        Ticket ticket =service.getTicketById(id);
        
        model.addAttribute("ticket", ticket);
        return "updateform";
    }
    
    
    
   //POST Mapping to process the form submission for updating the ticket
    @PostMapping("/update-ticket")
    public String updateTicket(@ModelAttribute Ticket updatedTicket, RedirectAttributes redirectAttributes) {
        
        service.updateTicket(updatedTicket);
        redirectAttributes.addFlashAttribute("message", "Ticket updated successfully");
        return "redirect:/home"; // Redirect to the home page after updating the ticket
    }
    
    
    // Controller method to display the details of a single ticket
    @GetMapping("/view")
    public String viewTicket(@RequestParam int id, Model model) {
        // Retrieve the ticket from the database based on the provided id
        Ticket ticket = service.getTicketById(id);
        // Add the ticket object to the model to display its details
        model.addAttribute("ticket", ticket);
        return "viewform"; // Return the view for displaying ticket details
    }
    
    
    //method to handle the delete operation for a ticket
    @GetMapping("/delete")
    public String deleteTicket(@RequestParam int id, RedirectAttributes redirectAttributes) {
        // Retrieve the ticket from the database based on the provided id
        Ticket ticket = service.getTicketById(id);
        
        if (ticket != null) {
            // Delete the ticket from the database
            service.delete(ticket);
            
            // Add a flash attribute to display a success message on the redirected page
            redirectAttributes.addFlashAttribute("message", "Ticket deleted successfully");
        } else {
            // If the ticket with the given ID does not exist.
            redirectAttributes.addFlashAttribute("error", "Ticket not found");
        }
        
        // Redirect the user to the home page or any other appropriate page
        return "redirect:/home";
    }
    
    
    @GetMapping("/viewrecord")
    public String viewRecord(@RequestParam int id, Model model) {
        // Retrieve the record from the database based on the provided id
        Ticket ticket = service.getTicketById(id);
        
        // Add the record object to the model
        model.addAttribute("ticket", ticket);
        
        return "viewform"; // Return the viewform html template
    }
    
    
    
    
    
    @PostMapping("/search")
    public String search(@ModelAttribute("query") String query, Model model) {
        List<Ticket> filteredList = service.filterByTitle(query);
        model.addAttribute("ticket", filteredList);
        return "home";
    }

    
    
    
}
