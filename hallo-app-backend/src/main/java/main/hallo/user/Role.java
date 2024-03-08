package main.hallo.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id; 
	private String authority; 
	 
	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Role(Integer id, String authority) {
		this.authority=authority;
		this.id=id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	
	   @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((authority == null) ? 0 : authority.hashCode());
	        return result;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        Role other = (Role) obj;
	        if (authority == null) {
	            if (other.authority != null)
	                return false;
	        } else if (!authority.equals(other.authority))
	            return false;
	        return true;
	    }
	

}
