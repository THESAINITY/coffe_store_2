package com.setec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.setec.entities.Booked;
import com.setec.repos.BookedRepo;
import com.setec.telegrambot.MyTelegramBot;

import org.springframework.ui.Model;



@Controller
public class MyController {
	//http://localhost:8080/
	
	@GetMapping({"/","/home"})
	public String home(Model mod) {
		Booked booked = new Booked(
		        1,                              // id
		        "Hak Tharoth",                  // name
		        "0784941089",                   // phoneNumber
		        "tharoth@gmail.com",          	// email
		        "10/08/2035",                   // date
		        "9:00AM",                       // time
		        2                             	// person
		    );
		    mod.addAttribute("booked", booked);
	//		    return "reservation";
		return "index";
	}
	
	
	@GetMapping("/about")
	public String about() {
	    return "about";
	}
	
	@GetMapping("/service")
	public String service() {
		return "service";
	}
	
	
	@GetMapping("/menu")
	public String menu() {
		return "menu";
	}
	
	@GetMapping("/reservation")
	public String reservation(Model mod) {
	    Booked booked = new Booked(
	        1,                              // id
	        "Hak Tharoth",                  // name
	        "0784941089",                   // phoneNumber
	        "tharoth@gmail.com",          	// email
	        "10/08/2035",                   // date
	        "9:00AM",                       // time
	        2                             	// person
	    );
	    mod.addAttribute("booked", booked);
	    return "reservation";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}
	
	@GetMapping("/testimonial")
	public String testimonial() {
	    return "testimonial";
	}
	
	
	@Autowired
	private BookedRepo bookedRepo;
	
	@Autowired
	private MyTelegramBot bot;
	
	@PostMapping("/success")
	public String success(@ModelAttribute Booked booked) {
		bookedRepo.save(booked);
		String message = String.format(
		        "🎉 Booking Done!\n\n" +
		        "🤓 Name: %s\n" +
		        "📞 Phone: %s\n" +
		        "✉️ Email: %s\n" +
		        "📅 Date: %s\n" +
		        "⏰ Time: %s\n" +
		        "👥 Number of People: %d\n\n" +
		        "Thanks May May for booking! 🫶🏿"
		        ,
		        booked.getName(),
		        booked.getPhoneNumber(),
		        booked.getEmail(),
		        booked.getDate(),
		        booked.getTime(),
		        booked.getPerson()
		    );

		    bot.message(message);
		    return "success";
	}
	
}
