package io.blockwavelabs.tree.service.userdetails;

import io.blockwavelabs.tree.auth.domain.User;
import lombok.Getter;
@Getter
public class UserAdapter extends CustomUserDetails{
    /**
     * https://sol-devlog.tistory.com/3
     * UserAdapter 를 따로 빼서 하는 이유 reference
     */

    private User user;

    public UserAdapter(User user) {
        super(user);
        this.user = user;
    }
}
