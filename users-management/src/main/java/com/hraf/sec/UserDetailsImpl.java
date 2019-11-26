package com.hraf.sec;

import com.hraf.dao.AppUserRepository;
import com.hraf.entities.AppUser;
import com.hraf.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsImpl implements UserDetailsService {
    @Autowired private AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("this is UserDetails");
        AppUser appUser = accountService.loadUserByUsername(s);
        if(appUser==null)
            throw new UsernameNotFoundException("this user not exist");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(r->{
            authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
        });
        return new User(appUser.getUsername(),appUser.getPassword(),authorities);
    }
}
