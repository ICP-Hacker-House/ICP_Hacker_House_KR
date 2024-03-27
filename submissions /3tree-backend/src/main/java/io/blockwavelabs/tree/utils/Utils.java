package io.blockwavelabs.tree.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@NoArgsConstructor
@Component
public class Utils {
    public String createDefaultUserIdWithEmail(String email){
        String[] split1_ = email.split("@");
        String[] split2_ = split1_[1].split("\\.");
        String id = split1_[0];
        String platform = split2_[0];

        return id + "_" + platform;
    }

    public String createRandomString(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

}
