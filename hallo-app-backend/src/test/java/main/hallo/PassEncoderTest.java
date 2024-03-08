package main.hallo;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
public class PassEncoderTest {

	@Test
	public void encode_password() {
		PasswordEncoder pass=new BCryptPasswordEncoder();
		System.out.println(pass.encode("1q2w3e4r"));
	}
}
