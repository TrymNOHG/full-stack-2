package edu.ntnu.idatt2105.backend.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Data transfer object for the response of the authentication endpoint.
 */

@Builder
public record AuthenticationResponseDTO(
        @JsonProperty("token") String token,
        @JsonProperty("token_expiration") String tokenExpiration,
        @JsonProperty("token_type") TokenType tokenType,
        @JsonProperty("username") String username
) {}
