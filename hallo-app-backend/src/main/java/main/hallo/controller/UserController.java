package main.hallo.controller;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import main.hallo.jwt.JwtUtil;
import main.hallo.user.User;
import main.hallo.user.UserDto;
import main.hallo.user.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "https://assignments.coderscampus.com"}, allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

//    @GetMapping("{email}")
//    public ResponseEntity<?> getUser(@PathVariable String email) {
//        Optional<User> userByUsername = userService.findUserByUsername(email);
//        
//        return ResponseEntity.ok(userByUsername);
//    }

 

    @PostMapping("/register")
    private ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
    	
//    	System.out.println(userDto);
//    	User user=userService.findUserByEmail(userDto.getUsername()).orElse(null);
//    	System.out.println("The user is searched for " +user);
//    	if (user==null) {
//        userService.createUser(userDto);
//        System.out.println("This is a new user created");
//       	return ResponseEntity.ok("User Registered Successfully");
//    	}else {
//    		return    new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    	}
    	
    	if(userService.createUser(userDto)) {
    		
    		return ResponseEntity.ok("User Registered Successfully");
    	}else{
    		return    new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}

    }

}
