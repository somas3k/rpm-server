package pl.edu.agh.im.remotepatientmonitor.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.im.remotepatientmonitor.domain.ApplicationUser;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("user")
public class UserController {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailService emailService;
    private JWTService jwtService;
    private static final Logger LOGGER = Logger.getLogger("Registration");

    @Autowired
    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService, JWTService jwtService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody ApplicationUser user) {
        return Optional
                .ofNullable(
                        Optional.ofNullable(userRepository.findByEmail(user.getEmail()))
                                .orElse(userRepository.findByEmail(user.getEmail()))
                )
                .map(u -> ResponseEntity.status(HttpStatus.CONFLICT).build())
                .orElseGet(() -> {
                    String token = jwtService.createSignedEmailToken(user.getEmail());
                    try {
                        ApplicationUser notEnabledNewUser = userRepository.save(user.withEncodedPassword(bCryptPasswordEncoder));
                        emailService.sendActivationMessage(user, token);
                        return ResponseEntity.ok(notEnabledNewUser.getId());
                    } catch (Throwable e) {
                        LOGGER.log(Level.SEVERE, "Something went wrong during registration process", e);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                });
    }

    @PostMapping("refresh")
    public ResponseEntity<UserLoginResponse> refreshToken(@RequestBody RefreshToken refreshToken) {
        String username = jwtService.getUsernameFromToken(refreshToken.getRefreshToken());
        String type = jwtService.getTypeFromToken(refreshToken.getRefreshToken());
        if (type.equals("refresh")) {
            String newToken = jwtService.createSignedLoginToken(username);
            return ResponseEntity.ok(new UserLoginResponse(newToken, refreshToken.getRefreshToken()));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("logout")
    public ResponseEntity logout() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("activate")
    public ResponseEntity activate(@RequestParam(value = "token") String token) {
        try {
            String username = jwtService.getUsernameFromToken(token);
            if (username != null) {
                ApplicationUser user = userRepository.findByEmail(username);
                if (user.getEnabled()) {
                    ResponseEntity.ok("Account already activated! Goto <a href=\"http://localhost:4200/auth/login\">Login page</a>");
                }
                user.setEnabled(true);
                userRepository.save(user);
                return ResponseEntity.ok("Account activated! :) Goto <a href=\"http://localhost:4200/auth/login\">Login page</a>");
            }
        } catch (Throwable e) {
            return ResponseEntity.ok("Something went wrong :(. Try to register another account. <a href=\"http://localhost:4200/auth/register\">Register page</a>");
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}

