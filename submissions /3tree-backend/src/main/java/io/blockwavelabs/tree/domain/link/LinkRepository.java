package io.blockwavelabs.tree.domain.link;

import io.blockwavelabs.tree.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUser(User user);

    Optional<List<Link>> findAllByUser_UserId(String userId);
    Optional<Link> findLinkById(Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE link SET link_title = :title, link_url = :url WHERE `index` = :id", nativeQuery = true)
    void updateLinkInfo(@Param(value = "title") String title,
                        @Param(value = "url") String url, @Param(value = "id") Long id);

    @Query(value = "DELETE FROM link WHERE `index` = :id",nativeQuery = true)
    void deleteById(@Param(value = "id")Long id);

    @Query(value = "INSERT INTO link(user_index, link_title, link_url) VALUES (?,?,?)", nativeQuery = true)
    void addLink(Long uid, String linkTitle, String linkUrl);

}
