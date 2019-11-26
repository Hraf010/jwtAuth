package com.hraf.sec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Origin",
                "*");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-RequestHeaders,authorization");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, authorization");
        if(httpServletRequest.getMethod().equals("OPTIONS")){
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
        else{
            String jwt = httpServletRequest.getHeader(SecurityParams.HEADER_NAME);
            System.out.println(jwt);
            if(jwt == null ||!jwt.startsWith(SecurityParams.HEADER_PREFIX)){
                System.out.println("**************************");
                filterChain.doFilter(httpServletRequest,httpServletResponse);
                return;
            }
            //cette instruction permet de signer le token avec notre secret
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
            //Apres on decode le JWT Pour get les informations (username , roles ...)
            DecodedJWT decodedJWT = verifier.verify(jwt.substring(SecurityParams.HEADER_PREFIX.length(),jwt.length()));
            String  username = decodedJWT.getSubject();
            System.out.println(username);
            List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(r->{
            authorities.add(new SimpleGrantedAuthority(r));
        });

            System.out.println(roles);
            //Maitenant on declare à spring que ce user est authentifié
            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(user);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
}


