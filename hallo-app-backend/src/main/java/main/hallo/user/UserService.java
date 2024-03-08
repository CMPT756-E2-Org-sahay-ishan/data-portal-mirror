package main.hallo.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.hallo.config.CustomPassword;
@Service
public class UserService {
	 @Autowired
	    private UserRepository userRepo;
	public Optional<User> findUserByEmail(String email) {
		 Optional<User> user =userRepo.findByUsername(email);
		return user;
	}

    public boolean createUser(UserDto userDto) {
        User newUser = new User();
        User userAlreadyExists=userRepo.findByUsername(userDto.getUsername()).orElse(null);
        if (userAlreadyExists==null) {
            newUser.setUsername(userDto.getUsername());
            newUser.setLastName(userDto.getLastName());
            newUser.setFirstName(userDto.getFirstName());
            
            String encodedPassword = CustomPassword.passwordEncoder().encode(userDto.getPassword());
            newUser.setPassword(encodedPassword);
            Role role= new Role(2, "ROLE_RESEACHER");
            Set<Role> targetSet = new HashSet<Role>(Arrays.asList(role));
            newUser.setAuthorities(targetSet);
            
            userRepo.save(newUser);
            return true;
        }
        else return false;
 
    }

}
