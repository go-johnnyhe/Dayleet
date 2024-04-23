package org.johnnyhe.dayleet.service;

import org.johnnyhe.dayleet.model.user;
import org.johnnyhe.dayleet.repository.userRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final userRepo myUserRepo;
    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    @Autowired
    public UserDetailsServiceImpl(userRepo myUserRepo) {
        this.myUserRepo = myUserRepo;
    }

    public void registerUser(user myUser) {
        myUserRepo.save(myUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<user> myUserOpt = myUserRepo.findByUsername(username);
        if (!myUserOpt.isPresent()) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        user myUser = myUserOpt.get();
        return new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), Collections.emptyList());
    }

}
