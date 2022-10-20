package com.Novare.AuthExample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyApiController {

    Logger logger = LoggerFactory.getLogger(MyApiController.class);

    @Autowired
    UserDetailsManager userDetailsManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/api/signup")
    public void singup(@RequestBody SignUpData data){
        try {
            UserDetails user = User.builder()
                    .username(data.getEmail())
                    .password(passwordEncoder.encode(data.getPass()))
                    .roles("USER")
                    .build();

            userDetailsManager.createUser(user);
            logger.info("User created " + data.getEmail() + " " + data.getPass());
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    // The /api/admin/** endpoint is restricted to the admin role.
    @PostMapping("/api/admin/signup")
    public void singupAdmin(@RequestBody SignUpData data){
        try {
            UserDetails user = User.builder()
                    .username(data.getEmail())
                    .password(passwordEncoder.encode(data.getPass()))
                    .roles("ADMIN")
                    .build();

            userDetailsManager.createUser(user);
            logger.info("User created " + data.getEmail() + " " + data.getPass());
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @GetMapping("/api/info")
    public String info(Authentication auth){
        return auth.getName();
    }

    @GetMapping("/api/check")
    public String check(){
        return "You need to be logged in to view this endpoint";
    }

    // The /api/admin/** endpoint is restricted to the admin role.
    @GetMapping("/api/admin/check")
    public String checkAdmin(){
        return "You need to be logged in as a admin to view this endpoint";
    }


    public static class SignUpData{
        private String fullname;
        private String email;
        private String pass;

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }
    }
}
