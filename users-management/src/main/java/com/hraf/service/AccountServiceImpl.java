package com.hraf.service;

import com.hraf.dao.AppRoleRepository;
import com.hraf.dao.AppUserRepository;
import com.hraf.entities.AppRole;
import com.hraf.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppRoleRepository appRoleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AppUser saveUser(String username, String password, String confirmedPassword) {
        AppUser user = appUserRepository.findByUsername(username);
        if(user != null)
            throw new RuntimeException("This user already exists");
        if(!password.equals(confirmedPassword)){
            System.out.println(password);
            System.out.println(confirmedPassword);
            throw new RuntimeException("please confirm your password");

        }

        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUser.setActive(true);
        appUserRepository.save(appUser);
        addRoleToUser(username,"USER");
        return appUser;
    }

    @Override
    public AppRole saveRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppRole appRole = appRoleRepository.findByRoleName(rolename);
        if(appRole == null){
            AppRole appRole1 = new AppRole();
            appRole1.setRoleName(rolename);
            appRole = appRoleRepository.save(appRole1);
        }
        AppUser appUser = appUserRepository.findByUsername(username);
        appUser.getRoles().add(appRole);
        // machi mochkil iLa madarnach save 7it deja les methods sont transactional donc mli aysali lmethod aydir commit
    }
}
