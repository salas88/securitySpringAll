package com.example.securite.security.auoth;

import com.example.securite.security.security.ApplicationUserRole;
import com.google.common.collect.Lists;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao{

    private final PasswordEncoder passwordEncoder;

    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUser()
                .stream()
                .filter(s -> username.equals(s.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUser(){
        List<ApplicationUser> applicationUserList = Lists.newArrayList(
                new ApplicationUser(
                    "linda", passwordEncoder.encode("password"),
                        ApplicationUserRole.ADMIN.getGrantedAuthority(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "annasmith", passwordEncoder.encode("password"),
                        ApplicationUserRole.STUDENT.getGrantedAuthority(),
                        true,

                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "tom", passwordEncoder.encode("password"),
                        ApplicationUserRole.ADMINISTRATION.getGrantedAuthority(),
                        true,

                        true,
                        true,
                        true
                )
        );
        return applicationUserList;
    }
}
