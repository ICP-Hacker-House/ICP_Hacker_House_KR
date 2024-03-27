package io.blockwavelabs.tree.config.security;

import io.blockwavelabs.tree.auth.domain.Role;
import io.blockwavelabs.tree.dto.user.response.UserResponseDto;
import io.blockwavelabs.tree.exception.SamTreeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Slf4j
@Getter
@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;
    private final Key key;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(
            @Value("${secret.jwt-secret-key}") String secretKey,
            @Value("${secret.access-token-expire-time}") long accessTime,
            @Value("${secret.refresh-token-expire-time}") long refreshTime,
            UserDetailsService userDetailsService){
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    protected String createToken(String email, Role auth, long tokenValid){
        // ex) sub : abc@gmail.com
        Claims claims = Jwts.claims().setSubject(email);

        // ex) auth : USER
        claims.put(AUTHORITIES_KEY,
                auth.getKey()
        );

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValid)) // token 만료시간
                .signWith(key, SignatureAlgorithm.HS256) // 키, 알고리즘
                .compact();
    }

    public String createAccessToken(String email, Role auth){
        String token = this.createToken(email, auth, ACCESS_TOKEN_EXPIRE_TIME);
        return token;
    }

    public String createRefreshToken(String email, Role auth){
        return this.createToken(email, auth, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String getUserEmailByToken(String token){
        // token 의 claim 의 sub 키에 이메일 값이 들어있다.
        return this.parseClaims(token).getSubject();
    }

    public UserResponseDto.TokenDto createTokenDTO(String accessToken, String refreshToken){
        return UserResponseDto.TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(BEARER_TYPE)
                .build();
    }

    // token 에서 Authentication 인스턴스 가져오기
    public Authentication getAuthentication(String accessToken) throws SamTreeException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.parseClaims(accessToken).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public int validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return 1;
        } catch (ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다.");
            return 2;
        } catch (Exception e){
            log.info("잘못된 토큰입니다.");
            return -1;
        }
    }

    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        }catch (ExpiredJwtException e){ // 만료된 토큰이더라도 일단 파싱을 함
            return e.getClaims();
        }
    }
}