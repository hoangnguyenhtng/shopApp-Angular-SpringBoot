package com.project.shopapp.filters;

import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            //filterChain.doFilter(request, response); //enable bypass
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                System.out.println("User roles: " + userDetails.getAuthorities());

                if (jwtTokenUtil.validationToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
                filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorired");
        }

    }

//    private boolean hasPermission(List<? extends GrantedAuthority> authorities, HttpServletRequest request) {
//        String requestURI = request.getRequestURI();
//        String requestMethod = request.getMethod();
//
//        // Chỉ cho phép người dùng với quyền "ADMIN" thực hiện DELETE trên /orders
//        if (requestURI.contains("/orders") && requestMethod.equals("DELETE")) {
//            return authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//        }
//
//        // Chỉ cho phép người dùng với quyền "ADMIN" hoặc "USER" thực hiện PUT trên /orders
//        if (requestURI.contains("/orders") && requestMethod.equals("PUT")) {
//            return authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//        }
//
//        // Chỉ cho phép người dùng với quyền "ADMIN" hoặc "USER" thực hiện GET trên /orders
//        if (requestURI.contains("/orders") && requestMethod.equals("GET")) {
//            return authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN") || authority.getAuthority().equals("ROLE_USER"));
//        }
//
//        // Thêm các quy tắc kiểm tra quyền khác tại đây
//
//        return true; // Cho phép truy cập nếu không có quy tắc nào khớp
//    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST")
        );
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getLeft()) &&
                    request.getMethod().equals(bypassToken.getRight())) {
                return true;
            }
        }
        return false;
    }
}
