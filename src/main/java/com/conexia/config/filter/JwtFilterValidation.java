package com.conexia.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.conexia.service.dto.LoggedUser;
import com.conexia.utils.jwt.JwtUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtFilterValidation extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;

  public JwtFilterValidation(JwtUtils jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  @Override
  protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                  @Nonnull HttpServletResponse response,
                                  @Nonnull FilterChain filterChain) throws ServletException, IOException {

    if (request.getServletPath().startsWith("/auth/")) {
      filterChain.doFilter(request, response);
      return;
    }

      String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

      if (jwtToken != null && jwtToken.startsWith("Bearer ")){
      jwtToken = jwtToken.substring(7);

      try {
        DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
        String username = jwtUtils.extractUsername(decodedJWT);
        String role = jwtUtils.getSpecifictClaim(decodedJWT, "role").asString();
        Long userId = jwtUtils.getSpecifictClaim(decodedJWT, "userId").asLong();

        LoggedUser loggedUser = new LoggedUser(userId, username, role);

        GrantedAuthority authority = new SimpleGrantedAuthority(role);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loggedUser, null, Collections.singletonList(authority));

        SecurityContextHolder.getContext().setAuthentication(authToken);

      } catch (Exception e){

        SecurityContextHolder.clearContext();
      }
    }

    filterChain.doFilter(request, response);
  }
}
