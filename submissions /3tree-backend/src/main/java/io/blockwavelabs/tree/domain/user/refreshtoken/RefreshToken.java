package io.blockwavelabs.tree.domain.user.refreshtoken;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tokenKey;

    @Column
    private String tokenValue;

    @Builder
    public RefreshToken(String tokenKey, String tokenValue){
        this.tokenKey = tokenKey;
        this.tokenValue = tokenValue;
    }
}
