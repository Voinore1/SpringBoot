package org.example.seeder;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.entities.RoleEntity;
import org.example.entities.UserEntity;
import org.example.entities.UserRoleEntity;
import org.example.repository.IRoleRepository;
import org.example.repository.IUserRepository;
import org.example.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class UserSeeder {
    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void seed() {
        if(userRepository.count() > 0) return;

        var admin = new UserEntity();
        admin.setRegisterTime(LocalDateTime.now());
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin = userRepository.save(admin);

        RoleEntity adminRole = roleRepository.findByName("ADMIN").orElseThrow();
        UserRoleEntity ur = new UserRoleEntity(null, admin, adminRole);
        userRoleRepository.save(ur);
    }
}
