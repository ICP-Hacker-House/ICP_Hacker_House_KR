package io.blockwavelabs.tree.config.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.blockwavelabs.tree.dto.oauth.GoogleOAuthTokenDto;
import io.blockwavelabs.tree.dto.oauth.GoogleUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOAuth {
    private final String googleLoginUrl = "https://accounts.google.com";
    private final String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    //private String googleRedirectUrl = "http://localhost:8080/public/users/login/google";
    private String googleRedirectUrl = "https://api.punkstation.xyz/public/users/login/google";
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    //
    public String getOauthRedirectURL() {
        String reqUrl = googleLoginUrl + "/o/oauth2/v2/auth?client_id=" + googleClientId + "&redirect_uri=" + googleRedirectUrl
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        return reqUrl;
    }

    // request token rest api로 요청
    public ResponseEntity<String> requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("redirect_uri", googleRedirectUrl);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }

    //
    public GoogleOAuthTokenDto getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        System.out.println("response.getBody() = " + response.getBody());
        GoogleOAuthTokenDto googleOAuthTokenDto = objectMapper.readValue(response.getBody(), GoogleOAuthTokenDto.class);
        return googleOAuthTokenDto;
    }

//    public ResponseEntity<String> requestUserInfo(GoogleOAuthTokenDto oAuthToken) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
//        System.out.println("response.getBody() = " + response.getBody());
//        return response;
//    }


        public ResponseEntity<String> requestUserInfo(String oAuthToken) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + oAuthToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
            System.out.println("response.getBody() = " + response.getBody());
            return response;
        }

    public GoogleUserInfoDto getUserInfo(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleUserInfoDto googleUserInfoDto = objectMapper.readValue(response.getBody(), GoogleUserInfoDto.class);
        return googleUserInfoDto;
    }

//    public GoogleOAuthTokenDto googleLogin(String accessToken) throws ParseException{
//        String googleLoginAPi = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
//        RestTemplate rest = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        String body = "";
//
//        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
//        ResponseEntity<String> responseEntity = rest.exchange(googleLoginAPi+accessToken, HttpMethod.GET, requestEntity, String.class);
//        HttpStatus httpStatus = responseEntity.getStatusCode();
//        int status = httpStatus.value();
//        String response = responseEntity.getBody();
//        System.out.println("Response status: " + status);
//        System.out.println(response);
//
//        JSONParser jsonParser = new JSONParser();
//        JSONObject json = (JSONObject) jsonParser.parse(response);
//
//        return new GoogleLoginDto((String)json.get("id"), (String)json.get("email"), (String)json.get("name"), (String)json.get("picture"));
//
//    }

}
