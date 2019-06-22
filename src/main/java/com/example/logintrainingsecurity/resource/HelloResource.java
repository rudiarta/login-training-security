package com.example.logintrainingsecurity.resource;


import com.example.logintrainingsecurity.model.CustomUserDetails;
import com.example.logintrainingsecurity.model.Users;
import com.example.logintrainingsecurity.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RequestMapping("/rest/hello")
@RestController
public class HelloResource {
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/all")
    public String hello() {
        return "Hello Youtube";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/secured/all")
    public String securedHello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        Collection<? extends GrantedAuthority> role = null;
        
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            role = authentication.getAuthorities();
        }

        Optional<Users> s = usersRepository.findByName(currentUserName);
        Users w = s.map(CustomUserDetails::new).get();

        return "Secured Hello "+ w.getLastName() +" "+role.toArray()[0];
    }

    @GetMapping("/secured/alternate")
    public String alternate() {
        return "alternate";
    }
}
