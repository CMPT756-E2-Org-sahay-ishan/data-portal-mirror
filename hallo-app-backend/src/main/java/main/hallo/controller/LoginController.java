package main.hallo.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import main.hallo.jwt.JwtUtil;
import main.hallo.user.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
@RestController
@RequestMapping("/api/auth/")
public class LoginController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtil jwtUtil;
	
	 @Value("${cookies.domain}")
	    private String domain;
	 
	@PostMapping("login")
	public ResponseEntity<?> 
	login (@RequestBody AuthenticationRequestCredential req){
		System.out.println("This is the object " + req);
		try {
			Authentication authenticate=authenticationManager
					.authenticate(
							new UsernamePasswordAuthenticationToken(
									req.getUsername(), req.getPassword())
							);
			User user=(User) authenticate.getPrincipal();
			user.setPassword(null);
			return ResponseEntity.ok()
					.header(HttpHeaders.AUTHORIZATION,jwtUtil.generateToken(user))
					.body(user);
					
					////NOTE: we can return somthing differnet on the body or response					
		}catch(BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	}
	
	@GetMapping ("/validate")
	public ResponseEntity<?> validateTokenForLogin(@RequestParam String token, @AuthenticationPrincipal User user){
		try {
			System.out.println("We are validating");
			Boolean isTokenValid=jwtUtil.validateToken(token, user);
			
			System.out.println(" Token is valid: "+isTokenValid);
			
			System.out.println("Token is: " +token);
			return ResponseEntity.ok(isTokenValid);
		}
		catch(ExpiredJwtException e) {
			return ResponseEntity.ok(false);
		}
	}
	
	////
    @GetMapping("/logout")
    public ResponseEntity<?> logout () {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .domain(domain)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString()).body("ok");
    }
}
