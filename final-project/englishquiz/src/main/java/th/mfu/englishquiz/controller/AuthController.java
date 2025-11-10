package th.mfu.englishquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.mfu.englishquiz.repository.UserRepository;
import th.mfu.englishquiz.entity.User;
import th.mfu.englishquiz.dto.LoginRequest;
import th.mfu.englishquiz.dto.RegisterRequest;
import th.mfu.englishquiz.dto.AuthResponse;
import th.mfu.englishquiz.dto.UserDto;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // register
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        // check that username is already or not
        if (userRepository.findByUsername(request.getUsername()) != null) {
            return new AuthResponse(false, "Username already exists", null);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // ❗ ยังไม่เข้ารหัสตอนนี้
        user.setLevel(1);
        user.setTotalScore(0);
        userRepository.save(user);

        UserDto userDTO = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getLevel(), user.getTotalScore());
        return new AuthResponse(true, "Registration successful", userDTO);
    }

    // login
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());

        if (user == null) {
            return new AuthResponse(false, "User not found", null);
        }

        // compare password
        if (user.getPassword().equals(request.getPassword())) {
            UserDto userDTO = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getLevel(), user.getTotalScore());
            return new AuthResponse(true, "Login successful", userDTO);
        } else {
            return new AuthResponse(false, "Incorrect password", null);
        }
    }
}

