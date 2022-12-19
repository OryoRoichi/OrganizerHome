package by.itstep.organizaer.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Пример авторизационного токена:
 * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
 */

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        /**
         * StringUtils.isBlank вернет true если строка вида:
         * 1. null
         * 2. ""
         * 3. "            "
         */
        if (StringUtils.isBlank(authHeader) || !authHeader.contains("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = authHeader.split(" ")[1];
        // TODO: Валидация токена сбор информации о пользователе
        filterChain.doFilter(request, response);
    }
}
