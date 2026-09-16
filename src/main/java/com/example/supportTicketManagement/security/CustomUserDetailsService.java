package com.example.supportTicketManagement.security;

import com.example.supportTicketManagement.entity.User;
import com.example.supportTicketManagement.exception.UserNotFoundException;
import com.example.supportTicketManagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Returns the currently logged-in user details from security context
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with this email "+ username + " not found"));

        return new CustomUserDetails(user);
    }
}
