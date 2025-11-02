package com.example.spring_community.Web.resolver;

import com.example.spring_community.Auth.annotation.AuthUser;
import com.example.spring_community.Auth.dto.AuthUserDto;
import com.example.spring_community.Auth.jwt.JwtUtils;
import com.example.spring_community.Exception.CustomException;
import com.example.spring_community.Exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtils jwtUtils;

    public AuthUserArgumentResolver(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class)
                && parameter.getParameterType().equals(AuthUserDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String auth = webRequest.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_TOKEN);
        }

        String accessToken = auth.substring(7);

        try {
            Claims claims = jwtUtils.parseAndValidate(accessToken);
            Long userId = claims.get("userId", Number.class).longValue();
            AuthUserDto authUserDto = new AuthUserDto(userId);
            return authUserDto;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

    }
}
