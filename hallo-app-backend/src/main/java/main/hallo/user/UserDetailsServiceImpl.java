package main.hallo.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import main.hallo.config.CustomPassword;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService{
@Autowired
private CustomPassword	 passwordEncoder;
@Autowired 
UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOpt=userRepo.findByUsername(username);
		return userOpt.orElseThrow(()-> new UsernameNotFoundException("Invalid Credential"));
	}
}
