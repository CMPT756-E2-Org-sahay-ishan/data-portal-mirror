package main.hallo.user;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
	@JsonProperty("firstName")
private String firstName;
	@JsonProperty("lastName")
private String lastName;
@Column(unique=true, nullable=false) 
private String username;
	@JsonProperty("password")
private String password;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserDto [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username + ", password="
				+ password + "]";
	}
	
}
