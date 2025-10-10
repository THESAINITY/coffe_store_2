package com.setec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.setec.entities.Booked;
import com.setec.repos.BookedRepo;
import com.setec.telegrambot.MyTelegramBot;

@Controller
public class MyController {
    
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);
    
    @GetMapping({"/","/home"})
    public String home(Model mod) {
        Booked booked = new Booked();
        mod.addAttribute("booked", booked);
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
        Booked booked = new Booked();
        mod.addAttribute("booked", booked);
        logger.info("Reservation page loaded");
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
    public String success(@ModelAttribute Booked booked, Model model) {
        try {
            logger.info("Received booking: Name={}, Phone={}", booked.getName(), booked.getPhoneNumber());
            
            // Basic validation
            if (booked.getName() == null || booked.getName().trim().isEmpty()) {
                model.addAttribute("error", "Name is required");
                model.addAttribute("booked", booked);
                return "reservation";
            }
            
            if (booked.getPhoneNumber() == null || booked.getPhoneNumber().trim().isEmpty()) {
                model.addAttribute("error", "Phone number is required");
                model.addAttribute("booked", booked);
                return "reservation";
            }
            
            // Save to database
            Booked savedBooked = bookedRepo.save(booked);
            logger.info("Booking saved with ID: {}", savedBooked.getId());
            
            // Create Telegram message
            String message = String.format(
                "🎉 New Booking!\n\n" +
                "🤓 Name: %s\n" +
                "📞 Phone: %s\n" +
                "✉️ Email: %s\n" +
                "📅 Date: %s\n" +
                "⏰ Time: %s\n" +
                "👥 People: %d\n\n" +
                "Thank you! 🫶🏿",
                booked.getName() != null ? booked.getName() : "Not provided",
                booked.getPhoneNumber() != null ? booked.getPhoneNumber() : "Not provided",
                booked.getEmail() != null ? booked.getEmail() : "Not provided",
                booked.getDate() != null ? booked.getDate() : "Not provided",
                booked.getTime() != null ? booked.getTime() : "Not provided",
                booked.getPerson() != null ? booked.getPerson() : 0
            );
            
            // Send to Telegram
            logger.info("Sending to Telegram...");
            bot.message(message);
            logger.info("Telegram message sent!");
            
            return "success";
            
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage(), e);
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("booked", booked);
            return "reservation";
        }
    }
    
    // Simple test endpoint
    @GetMapping("/test-telegram")
    public String testTelegram(Model model) {
        try {
            String testMessage = "🤖 Test from Render - " + java.time.LocalDateTime.now();
            bot.message(testMessage);
            model.addAttribute("message", "Test sent to Telegram!");
        } catch (Exception e) {
            model.addAttribute("message", "Test failed: " + e.getMessage());
        }
        return "test-result";
    }
}