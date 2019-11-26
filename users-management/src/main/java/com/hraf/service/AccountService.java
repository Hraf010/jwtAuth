package com.hraf.service;

import com.hraf.entities.AppRole;
import com.hraf.entities.AppUser;

public interface AccountService {
    public AppUser saveUser(String username , String password , String confirmedPassword);
    public AppRole saveRole(AppRole appRole);
    public AppUser loadUserByUsername(String username );
    public void addRoleToUser(String username , String rolename);
}
