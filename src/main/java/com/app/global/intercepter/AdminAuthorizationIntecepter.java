package com.app.global.intercepter;

import com.app.domain.member.constant.Role;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.service.TokenManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AdminAuthorizationIntecepter implements HandlerInterceptor {
    private final TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Access token에 있는 Role이 ADMIN이면 true를, 아니라면 예외를 발생한다.
        String authorizationHeader = request.getHeader("Authorization");
        String accessToken = authorizationHeader.split(" ")[1];
        // 인증 인터셉터에서 값이 있는지 검사했기 때문에 값이 있고, Bearer 타입인지 검사하지 않는다.
        Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
        String role = (String) tokenClaims.get("role");
        if (!Role.ADMIN.equals(Role.valueOf(role))){
            throw new AuthenticationException(ErrorCode.FORBIDDEN_ADMIN);
        }
        return true;
    }
}
