package main.hallo.apiKey;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class ApiKeyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader("Api-Key");

        if (isValidApiKey(apiKey)) {
            Authentication authentication = new ApiKeyAuthentication(apiKey);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidApiKey(String apiKey) {
        // Implement your validation logic here
        // You can check against a database, properties file, or any other source
        // Return true if the API key is valid, false otherwise
        return apiKey != null && apiKey.equals("ivWNqWY1UJG1CUm2GcML066H");
    }
}
