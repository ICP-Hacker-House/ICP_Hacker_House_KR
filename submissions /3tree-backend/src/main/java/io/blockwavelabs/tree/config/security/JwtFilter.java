package io.blockwavelabs.tree.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
//@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String excludePath = "/public"; // TODO: JWT 안쓰는 API 추가 필요
        String path = request.getRequestURI();
        return path.startsWith(excludePath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String token = resolveToken(request);

        log.info("token = {}", token);
        if(StringUtils.hasText(token)){
            int flag = jwtTokenProvider.validateToken(token);
            if(flag == 1) { // 유효한 토큰
                this.setAuthentication(token);
            } else if (flag == 2) {
                log.error("Expired JWT TOKEN[ {} ], plz reissuing", token);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                Map<String, Object> body = new LinkedHashMap<>();
                body.put("code", HttpStatus.UNAUTHORIZED.value());
                body.put("message", "access_token 이 만료되었습니다.");

                new ObjectMapper().writeValue(response.getOutputStream(), body);
            } else {
                log.error("Invalid JWT TOKEN [ {} ]",token);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                Map<String, Object> body = new LinkedHashMap<>();
                body.put("code", HttpStatus.UNAUTHORIZED.value());
                body.put("message", "유효하지 않은 access_token 입니다.");

                new ObjectMapper().writeValue(response.getOutputStream(), body);
            }
        }
        filterChain.doFilter(request,response);
    }

    /**
     *
     * @param token
     * 토큰이 유효한 경우 SecurityContext에 저장
     */
    private void setAuthentication(String token){
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // Request Header 에서 토큰 정보 꺼내오기
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }
}
