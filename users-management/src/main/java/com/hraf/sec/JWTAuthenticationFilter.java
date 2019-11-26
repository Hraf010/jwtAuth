package com.hraf.sec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hraf.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AppUser user ;
        try {
            System.out.println(request.getInputStream());
            user = new ObjectMapper().readValue(request.getInputStream(),AppUser.class);
        }
        catch (Exception e){
            throw  new RuntimeException(e);
        }

        System.out.println("*****************************");
        System.out.println("username : "+user.getUsername());
        System.out.println("pass : "+user.getPassword());

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
    }

    // Had Lmethod hiya fach kangeneriw Token o kan7oto fih claims li bghina (Roles , ....)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User springUser = (User)authResult.getPrincipal(); // kanJibo Resultat dyal User mn AuthResult
        List<String> roles = new ArrayList<>();

        authResult.getAuthorities().forEach(a->{
            roles.add(a.getAuthority());
        });
        String jwt = JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(springUser.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[roles.size()])) //permet de convertir roles en tableau de string
                .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION))
                 .sign(Algorithm.HMAC256(SecurityParams.SECRET));
        response.addHeader(SecurityParams.HEADER_NAME,SecurityParams.HEADER_PREFIX+jwt);



    }
}
