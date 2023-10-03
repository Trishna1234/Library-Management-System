package com.trishna.library.services;

import com.trishna.library.exceptions.user.UserAlreadyExistsException;
import com.trishna.library.models.SecuredUser;
import com.trishna.library.repositories.UserRepository;
import com.trishna.library.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public SecuredUser save(SecuredUser securedUser, String userType) {
        String encryptedPwd = encoder.encode(securedUser.getPassword());
        String authorities = Utils.getAuthoritiesForUser().get(userType);

        securedUser.setAuthorities(authorities);
        securedUser.setPassword(encryptedPwd);
        if(userRepository.findByUsername(securedUser.getUsername()) != null)
            throw new UserAlreadyExistsException("User already exists");
        return userRepository.save(securedUser);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
