package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.config.security.JwtService;
import org.example.dtos.UserAuthDto;
import org.example.dtos.UserRegisterDto;
import org.example.entities.RoleEntity;
import org.example.entities.UserEntity;
import org.example.entities.UserRoleEntity;
import org.example.repository.IRoleRepository;
import org.example.repository.IUserRepository;
import org.example.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final FileService fileService;

    @Value("${google.api.userinfo}")
    private String googleUserInfoUrl;

    // Реєстрація нового користувача
    public void registerUser(UserRegisterDto dto) {
        var username = dto.getUsername();
        var password = dto.getPassword();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username '" + username + "' already exists!");
        }
        if (username == null || username.length() < 3) {
            throw new RuntimeException("Username is less than 3 characters!");
        }
        if (password == null || password.length() < 6) {
            throw new RuntimeException("Password is less than 6 characters!");
        }

        var userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setRegisterTime(LocalDateTime.now());

        //set image
        var imageFile = dto.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()){
            var imagePath = fileService.load(imageFile);
            userEntity.setImage(imagePath);
        }

        userEntity = userRepository.save(userEntity);

        RoleEntity userRole = roleRepository.findByName("USER").orElseThrow();
        UserRoleEntity ur = new UserRoleEntity(null, userEntity, userRole);
        userRoleRepository.save(ur);
    }

    // Аутентифікація користувача
    public String authenticateUser(UserAuthDto dto) {
        var foundUser = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid login or password!"));

        if (!passwordEncoder.matches(dto.getPassword(), foundUser.getPassword())) {
            throw new RuntimeException("Invalid login or password!");
        }
        // Генерація JWT токену
        return jwtService.generateAccessToken(foundUser);
    }

    public String signInGoogle(String access_token) throws IOException {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + access_token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(googleUserInfoUrl, HttpMethod.GET, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Invalid google response");
        }
        var mapper = new ObjectMapper();
        Map<String, String> userInfo = mapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {});
        var userEntity = userRepository.findByUsername(userInfo.get("email"))
                .orElse(null); // Порожній об'єкт

        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setUsername(userInfo.get("email"));
            userEntity.setPassword("");

            userEntity.setRegisterTime(LocalDateTime.now());
            userEntity.setGoogleUser(true);
            var pictureUrl = userInfo.get("picture");
            if (pictureUrl != null){
                userEntity.setImage(fileService.load(pictureUrl));
            }
            userEntity = userRepository.save(userEntity);

            RoleEntity userRole = roleRepository.findByName("USER").orElseThrow();
            UserRoleEntity ur = new UserRoleEntity(null, userEntity, userRole);
            userRoleRepository.save(ur);
        }
        return jwtService.generateAccessToken(userEntity);
    }
}