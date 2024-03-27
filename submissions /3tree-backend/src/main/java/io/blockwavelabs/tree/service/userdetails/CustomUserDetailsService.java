package io.blockwavelabs.tree.service.userdetails;

import io.blockwavelabs.tree.auth.domain.User;
import io.blockwavelabs.tree.auth.infrastructure.UserRepository;
import io.blockwavelabs.tree.exception.SamTreeException;
import io.blockwavelabs.tree.exception.type.UserExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws SamTreeException {
        log.debug("CustomUserDetailsService -> email = {}", email);
        User user = userRepository.findBySocialId(email)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));

        return new UserAdapter(user);
    }

    @Transactional(readOnly = true)
    public User getUser(String email) throws SamTreeException {
        return userRepository.findBySocialId(email)
                .orElseThrow(() -> new SamTreeException(UserExceptionType.NOT_FOUND_USER));
    }
}
