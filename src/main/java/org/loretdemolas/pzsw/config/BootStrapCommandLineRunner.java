package org.loretdemolas.pzsw.config;

import org.loretdemolas.pzsw.entity.User;
import org.loretdemolas.pzsw.entity.Role;
import org.loretdemolas.pzsw.entity.RoleName;
import org.loretdemolas.pzsw.repository.RoleRepository;
import org.loretdemolas.pzsw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class BootStrapCommandLineRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public BootStrapCommandLineRunner(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN);
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName(RoleName.ADMIN);
            roleRepository.save(adminRole);
        }

        Role memberRole = roleRepository.findByName(RoleName.MEMBER);
        if (memberRole == null) {
            memberRole = new Role();
            memberRole.setName(RoleName.MEMBER);
            roleRepository.save(memberRole);
        }
        // Create user with both roles
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(memberRole);

        Optional<User> firstUser = userRepository.findByUsername("loretdemolas");
        if (firstUser.isEmpty()) {
            String rawPassword = "Seba5054";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            User user = new User("loretdemolas", encodedPassword, roles);
            userRepository.save(user);
        }
    }
}
