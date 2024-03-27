package io.blockwavelabs.tree.auth.infrastructure;

import io.blockwavelabs.tree.auth.domain.SocialPlatformType;
import io.blockwavelabs.tree.auth.domain.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {

    // CREATE

    // READ
    Optional<SocialUser> findBySocialId(String socialId);
    Optional<SocialUser> findByUserIdAndSocialPlatformType(Long userId, SocialPlatformType socialPlatformType);
    List<SocialUser> findAllByUserIdAndIsConnected(Long userId, Boolean isConnected);
    boolean existsBySocialId(String socialId);

    // UPDATE

    // DELETE
}
