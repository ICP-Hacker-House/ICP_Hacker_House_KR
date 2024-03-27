package io.blockwavelabs.tree.domain.user.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByTokenKey(String key);
    Optional<RefreshToken> findRefreshTokenByTokenValue(String value);

    boolean existsByTokenKey(String key);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE RefreshToken rt SET rt.tokenValue = :value WHERE rt.tokenKey = :key")
    void updateRefreshTokenByKey(@Param(value = "key") String key,
                                 @Param(value = "value") String value);
}
