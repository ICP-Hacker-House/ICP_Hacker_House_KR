package io.blockwavelabs.tree.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //전처리 과정 - HttpServletRequest와 HttpServletResponse를 캐시 가능하도록 래핑해준다.
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);


        //전, 후 처리의 기준이되는 메소드
        //filter의 동작에 httpServletRequest, httpServletResponse를 이용한다.
        filterChain.doFilter(httpServletRequest, httpServletResponse);


        //후 처리 과정

        //request 요청으로 어떤 uri가 들어왔는지 확인
        String uri = httpServletRequest.getRequestURI();

        //request 내용 확인
        String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("URI: {}, Request: {}", uri, reqContent);


        // response 내용 상태 정보, 내용 확인
        int httpStatus = httpServletResponse.getStatus();
        String resContent = new String(httpServletResponse.getContentAsByteArray());

        //주의 : response를 클라이언트에서 볼 수 있도록 하려면 response를 복사해야 한다. response를 콘솔에 보여주면 내용이 사라진다.
        httpServletResponse.copyBodyToResponse();

        log.info("Status: {}, Response: {}", httpStatus, resContent);
    }
}
