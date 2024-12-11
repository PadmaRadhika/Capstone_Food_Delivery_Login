package com.example.fooddeliverylogin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fooddeliverylogin.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Allow CORS from Angular app
public class AuthController {
	
	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	 
	 //For admin login into admin portal, credentials are saved in properties file.
	 //When the functionality is extended to User login, it can be moved to user database table.
	 @Value("${admin.username}")
	 private String username;
	 @Value("${admin.password}")
	 private String password;

	    @PostMapping("/login")
	    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
	        if (authenticate(user)) {
	            String token = Jwts.builder()
	                    .setSubject(user.getUsername())
	                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
	                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
	                    .compact();
	            
	            Map<String, String> response = new HashMap<>();
	            response.put("token", token);
	            return ResponseEntity.ok(response);
	        } else {
	        	return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
	        }
	    }

	    private boolean authenticate(User user) {
	        // Perform actual authentication here
	    	System.out.println("**username::"+username+", password::"+password);
	        return username.equals(user.getUsername()) && password.equals(user.getPassword());
	    }

}
