package com.example.bookface.registration;

import com.example.bookface.appuser.AppUser;
import com.example.bookface.appuser.AppUserRole;
import com.example.bookface.appuser.AppUserService;
import com.example.bookface.email.EmailSender;
import com.example.bookface.registration.token.ConfirmationToken;
import com.example.bookface.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        String token = appUserService.signUpUser(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER)
        );
        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link));
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }

    private String buildEmail(String name, String link) {
        return "<div style='font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;'>" +
                "<div style='background-color: #ffffff; max-width: 600px; margin: 0 auto; padding: 20px; border-radius: 5px;'>" +
                "<h2 style='color: #007BFF;'>Confirm your email</h2>" +
                "<p>Hi " + name + ",</p>" +
                "<p>Thank you for registering. Please click on the below link to activate your account:</p>" +
                "<p><a href='" + link + "' style='background-color: #007BFF; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 5px;'>" +
                "Activate Now</a></p>" +
                "<p>Link will expire in 30 minutes.</p>" +
                "<p>See you soon</p>" +
                "</div>" +
                "</div>";
    }
}
