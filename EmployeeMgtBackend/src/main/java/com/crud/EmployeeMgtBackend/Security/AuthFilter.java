package com.crud.EmployeeMgtBackend.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
    public class AuthFilter extends OncePerRequestFilter {
        @Autowired
        private UserDetailsService userDetailsService;
        @Autowired
        private JwtTokenHelper jwtTokenHelper;
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {

//        1. get token------------
            String requestToken=request.getHeader("Authorization");

            String token=null;
            String username=null;

            if(requestToken!=null && requestToken.startsWith("Bearer")){
                token=requestToken.substring(7);
                try{
                    username=this.jwtTokenHelper.getUsernameFromToken(token);
                }
                catch (IllegalArgumentException e){
                    System.out.println ("No username found");
                }
                catch (ExpiredJwtException e){
                    System.out.println("jwt token expired");
                }
                catch (MalformedJwtException e){
                    System.out.println("Invalid token");
                }
            }
            else{
                System.out.println ("Jwt token does not begin with bearer");
            }

            UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
            System.out.println(userDetails);
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                if(jwtTokenHelper.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new
                            UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken );
                }
                else{
                    System.out.println("Invalid jwt token");
                }
            }
            else {
                System.out.println("username is null or security context Holder is not null");
            }

            filterChain.doFilter(request,response);
        }
    }
