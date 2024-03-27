package io.blockwavelabs.tree.domain.profileDecorate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class ProfileDecorate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("COLOR")
    private BackgroundTypeEnum backgroundType;

    @ColumnDefault("#ffffff")
    private String backgroundColor;

    private String backgroundImg;

    @ColumnDefault("#ffffff")
    private String buttonColor;

    @ColumnDefault("#000000")
    private String buttonFontColor;

    @ColumnDefault("#000000")
    private String fontColor;

    // 원래는  user 테이블의 user_id를 바라보고 있는 친구인데, pk로 참조되어있지 않아서 기본 column으로 냅둠
    private String userId;

    @Builder
    public ProfileDecorate(String userId){
        this.userId = userId;
    }
}
