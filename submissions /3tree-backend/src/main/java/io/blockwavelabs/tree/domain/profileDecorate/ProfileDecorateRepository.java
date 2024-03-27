package io.blockwavelabs.tree.domain.profileDecorate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileDecorateRepository extends JpaRepository<ProfileDecorate, Long>{
    Optional<ProfileDecorate> findByUserId(String userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE profile_decorate SET background_type = :type, background_color = :bgcolor," +
            "background_img = :bgimg, button_color = :bcolor, button_font_color = :bfcolor, font_color = :fcolor" +
            " WHERE `index` = :id", nativeQuery = true)
    void updateProfileDecorate(@Param(value = "type") String type, @Param(value = "bgcolor") String bgcolor,
                               @Param(value = "bgimg") String bgimg, @Param(value = "bcolor") String bcolor,
                               @Param(value = "bfcolor") String bfcolor, @Param(value = "fcolor") String fcolor,
                               @Param(value = "id") Long id);
}