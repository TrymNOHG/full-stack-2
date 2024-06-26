package edu.ntnu.idatt2105.backend.service.security;

import edu.ntnu.idatt2105.backend.config.UserConfig;
import edu.ntnu.idatt2105.backend.dto.security.RSAKeyPairDTO;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

/**
 * Service class for managing JWT tokens.
 *
 * @version 1.0 31.03.2024
 * @author Brage Halvorsen Kvamme
 */
@RequiredArgsConstructor
@Service
public class JWTTokenService {

    final private UserRepository userRepository;
    final private RSAKeyPairDTO rsaKeyPairDTO;

    /**
     * Gets the email from a JWT token.
     *
     * @param jwt The JWT token.
     * @return The enail of the user.
     */
    public String getUserEmail(Jwt jwt) {
        return jwt.getSubject();
    }

    /**
     * Gets the email from a JWT token as a string.
     *
     * @param token The JWT token as a string.
     * @return The email of the user.
     */
    public String getUserEmail(String token) {
        return getJwt(token).getSubject();
    }

    /**
     * Gets the JWT token object from a string containing the jwt.
     *
     * @param token The JWT token as a string.
     * @return The JWT token.
     */
    public Jwt getJwt(String token) {
        return NimbusJwtDecoder.withPublicKey(rsaKeyPairDTO.rsaPublicKey()).build().decode(token);
    }

    /**
     * Gets the user id from a JWT token.
     *
     * @param jwt The JWT token.
     * @return The user id.
     */
    public long getUserId(Jwt jwt) {
        return jwt.getClaim("id");
    }

    /**
     * Gets the user details from the email.
     *
     * @param email The email of the user.
     * @return The user details.
     * @throws IOException If the user is not found.
     */
    public UserDetails getUserDetails(String email) throws IOException {
        return userRepository
                .findByEmail(email)
                .map(UserConfig::new)
                .orElseThrow(()-> new IOException("User not found"));
    }

    /**
     * Gets the user details from the user id.
     *
     * @param userId The id of the user.
     * @return The user details.
     * @throws IOException If the user is not found.
     */
    public UserDetails getUserDetails(long userId) throws IOException {
        return userRepository
                .findById(userId)
                .map(UserConfig::new)
                .orElseThrow(()-> new IOException("User not found"));
    }

    /**
     * Checks if the token is valid. The token is valid if it is not expired and the subject
     * is the same as the email of the user.
     *
     * @param jwtToken The JWT token.
     * @param userDetails The user details.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isValidToken(Jwt jwtToken, UserDetails userDetails) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isAfter(Instant.now())
                && jwtToken.getSubject().equals(userDetails.getUsername());
    }

    /**
     * Checks if the token is valid. The token is valid if it is not expired.
     *
     * @param jwtToken The JWT token.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isValidToken(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isAfter(Instant.now());
    }
}
