package com.quadballholic.backend.authService.service;

import com.quadballholic.backend.authService.api.ResetPasswordRequest;
import com.quadballholic.backend.authService.api.SignInRequest;
import com.quadballholic.backend.authService.api.SignInResponse;
import com.quadballholic.backend.authService.api.SignUpRequest;
import com.quadballholic.backend.authService.enums.EnumTokenType;
import com.quadballholic.backend.authService.entity.EntityToken;
import com.quadballholic.backend.common.service.EmailService;
import com.quadballholic.backend.common.util.JwtUtils;
import com.quadballholic.backend.userService.api.RegisterUserCommand;
import com.quadballholic.backend.userService.api.UserServiceAPI;
import com.quadballholic.backend.userService.enums.EnumUserRoleName;
import com.quadballholic.backend.userService.enums.EnumUserStatus;
import com.quadballholic.backend.userService.entity.EntityRole;
import com.quadballholic.backend.userService.entity.EntityUser;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    final UserServiceAPI userService;
    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;
    final TokenService tokenService;
    final EmailService emailService;

    @Getter
    @Value("${auth.activationTokenExpirationMs}")
    private long activationTokenExpirationMs;

    @Getter
    @Value("${auth.passwordResetTokenExpirationMs}")
    private long passwordResetTokenExpirationMs;

    public SignInResponse signIn(SignInRequest signInRequest){

        String requestEmail = signInRequest.email();
        String requestPassword = signInRequest.password();
        Optional <EntityUser> u = userService.findEntityUserByEmail(requestEmail);
        if(u.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email/Password is invalid please try again");
        }

        EntityUser user = u.get();
        if(!passwordEncoder.matches(requestPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email/Password is invalid please try again");
        }

        if(user.getStatus() == EnumUserStatus.WAITING_EMAIL_CONFIRMATION) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "To access your account please confirm your email");
        }

        String accessTokenStr = jwtUtils.generateToken(user.getEmail());

        List<EnumUserRoleName> userRoles = user.getRole().stream()
                .map(EntityRole::getRoleName).toList();

        return new SignInResponse(
                        accessTokenStr,
                        jwtUtils.getAccessTokenExpirationMs(),
                        new SignInResponse.UserInfo(
                                user.getId(),
                                user.getEmail(),
                                user.getName(),
                                user.getSurname(),
                                userRoles
                        )
                );
    }

    public Long signUp(SignUpRequest signUpRequest){

        String requestEmail = signUpRequest.email();
        if(userService.userExists(requestEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email address already exists");
        }

        Long userId = userService.registerUser(
                new RegisterUserCommand(
                        signUpRequest.name(),
                        signUpRequest.surname(),
                        signUpRequest.email(),
                        passwordEncoder.encode(signUpRequest.password())
                )
        );

        EntityToken token  = tokenService.createToken(userId, EnumTokenType.ACCOUNT_ACTIVATION,activationTokenExpirationMs);
        emailService.sendAccountActivationEmail(requestEmail,token.getToken());
        return userId;
    }

    @Transactional
    public void activateUser(String tokenStr){

        EntityToken token = tokenService.validateToken(tokenStr);
        token.useToken();
        userService.activateUser(token.getUserId());

    }

    public void requestPasswordReset(String email){
        Optional<EntityUser> userOpt = userService.findEntityUserByEmail(email);
        if (userOpt.isPresent()) {
            EntityToken token  = tokenService.createToken(userOpt.get().getId(), EnumTokenType.PASSWORD_RESET, passwordResetTokenExpirationMs);
            emailService.sendPasswordResetEmail(email,token.getToken());
        }
    }

    @Transactional
    public void completePasswordReset(ResetPasswordRequest resetPasswordRequest){
        if(!resetPasswordRequest.newPassword().equals(resetPasswordRequest.newPasswordConfirmation())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password confirmation is not matching password");
        }
        EntityToken token = tokenService.validateToken(resetPasswordRequest.token());
        token.useToken();
        userService.resetUserPassword(token.getUserId(),passwordEncoder.encode(resetPasswordRequest.newPassword()));
    }
}
