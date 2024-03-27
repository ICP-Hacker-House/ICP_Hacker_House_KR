package io.blockwavelabs.tree.auth.infrastructure;

import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.auth.domain.LanguageEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUserId(String userId);
    Optional<User> findBySocialId(String socialId);
    Optional<User> findByDid(String did);
    Optional<User> findByFrontKey(String frontKey);
    Boolean existsBySocialId(String socialId);
    Boolean existsByUserId(String userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE User u SET u.profileImg = :img, u.profileName = :name, u.profileBio = :bio WHERE u.userId = :uid")
    void updateUserProfile(@Param(value = "uid")String uid, @Param(value = "img")String img,
                                @Param(value = "name")String name, @Param(value = "bio") String bio);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE User u SET u.language = :language WHERE u.userId = :uid")
    void updateUserLanguage(@Param(value = "uid")String uid, @Param(value = "language") LanguageEnum language);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE User u SET u.userId = :nid WHERE u.userId = :uid")
    void updateUserId(@Param(value = "uid")String uid, @Param(value = "nid")String nid);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM User u WHERE u.userId = :uid")
    void deleteUserByUserId(@Param(value = "uid")String uid);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE User u SET u.frontKey = :frontKey WHERE u.userId = :uid")
    void updateFrontKey(@Param(value = "uid")String uid, @Param(value = "frontKey")String frontKey);
}
