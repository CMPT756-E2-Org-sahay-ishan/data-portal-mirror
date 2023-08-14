package main.hallo.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Role implements GrantedAuthority{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; 
	private String authority; 
	 @ManyToOne
	    @JsonIgnore
	private User user;
	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Role(String authority) {
		this.authority=authority;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	

}
