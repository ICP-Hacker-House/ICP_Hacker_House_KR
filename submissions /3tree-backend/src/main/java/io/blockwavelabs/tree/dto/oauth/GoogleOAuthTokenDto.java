package io.blockwavelabs.tree.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GoogleOAuthTokenDto {
    private String access_token;
    private Integer expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
