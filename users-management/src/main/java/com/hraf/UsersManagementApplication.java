package com.hraf;

import com.hraf.entities.AppRole;
import com.hraf.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class UsersManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersManagementApplication.class, args);
    }
    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            accountService.saveRole(new AppRole("USER"));
            accountService.saveRole(new AppRole("ADMIN"));
            Stream.of("user1","user2","user3","admin").forEach(u->{
                accountService.saveUser(u,"1234","1234");
            });
            accountService.addRoleToUser("admin","ADMIN");
        };

    }
    @Bean
    BCryptPasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

}
