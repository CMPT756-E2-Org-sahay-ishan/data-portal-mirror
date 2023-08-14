package main.hallo.jwt;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import main.hallo.user.UserRepository;



@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private UserRepository userRepo;
	@Autowired 
	private JwtUtil jwtUtil; 
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//Get the authorization header and validate
		final String header =request.getHeader(HttpHeaders.AUTHORIZATION);
		if (!StringUtils.hasText(header)||(StringUtils.hasText(header)&&!header.startsWith("Bearer "))) {
			filterChain.doFilter(request, response);
			return;
		}
		final String token=header.split(" ")[1].trim();
		//Get User identity from token
		UserDetails userDetails=
				userRepo.findByUsername(jwtUtil.getUsernameFromToken(token)).orElse(null);
		
		
		//Get Jwt Token and Validate
//		if(!jwtUtil.validateToken(token, userDetails)) {
//			filterChain.doFilter(request, response);
//			return;
//		}
		if(!jwtUtil.validateToken(token, userDetails)) {
			filterChain.doFilter(request, response);
			return;
		}		
		UsernamePasswordAuthenticationToken authentication=new 
				UsernamePasswordAuthenticationToken(
						userDetails, null, 
						userDetails==null? List.of():userDetails.getAuthorities()
						);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	
		//This is where the authentication happens
	SecurityContextHolder.getContext().setAuthentication(authentication);
	filterChain.doFilter(request, response);
	
	}
	
}
	
	