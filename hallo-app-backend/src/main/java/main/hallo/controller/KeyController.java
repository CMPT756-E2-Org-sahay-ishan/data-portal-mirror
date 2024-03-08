package main.hallo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/key/")
public class KeyController {
	
	@GetMapping("secure")
	public String securedendpont() {
		return "Hello World!";
	}

}
