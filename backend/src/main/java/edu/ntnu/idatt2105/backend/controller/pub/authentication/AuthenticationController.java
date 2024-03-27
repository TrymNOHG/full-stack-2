package edu.ntnu.idatt2105.backend.controller.pub.authentication;

import edu.ntnu.idatt2105.backend.dto.security.AuthenticationResponseDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserRegisterDTO;
import edu.ntnu.idatt2105.backend.service.AuthenticationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.logging.Logger;

/**
 * This controller provides the public endpoint for authentication. This controller is used for
 * login, signup and retrieval of access tokens from refresh tokens.
 *
 * @author brage
 * @version 1.0 24.03.2024
 */
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/public/auth")
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class AuthenticationController implements IAuthenticationController {

    private final AuthenticationService authenticationService;
    private final Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    /**
     * Endpoint for logging in. This endpoint returns the access and refresh token. The access token is returned
     * in the response body and the refresh token is returned as a cookie.
     *
     * @param authentication The authentication object
     * @param httpServletResponse The http response
     * @return The access token
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully logged in"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @Operation(
            summary = "Login the user with username and password",
            description = """
                    Logs in a user and returns the access and refresh token. Send the username and password in the
                    request body as basic access authentication.
                    
                    Here is a resource on the basic access authentication:
                    
                    
                    https://en.wikipedia.org/wiki/Basic_access_authentication#:~:text=In%20basic%20HTTP%20authentication%2C%20a,HTTP%201.0%20specification%20in%201996

                    The response body contains the access token and the refresh token is returned as a cookie.
                    """,
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(Authentication authentication, HttpServletResponse httpServletResponse) {
        logger.info("Starting login process.");
        return ResponseEntity.ok(authenticationService.getTokensFromAuth(authentication, httpServletResponse));
    }

    /**
     * Endpoint for signing up. This endpoint registers a new user and returns the access and refresh token.
     * The access token is returned in the response body and the refresh token is returned as a cookie.
     *
     * @param userRegisterDto The user register dto
     * @param httpServletResponse The http response
     * @param bindingResult The binding result
     * @return The access token
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User registered successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @Operation(summary = "Register a new user",
            description = """
                    Registers a new user and returns the access and refresh tokens. The access token is returned in the response body, and the refresh token is returned as a cookie. Provide user registration details in the request body.
                    """)
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponseDTO> signup(@Valid @RequestBody UserRegisterDTO userRegisterDto,
                                          HttpServletResponse httpServletResponse, BindingResult bindingResult){
        logger.info("Calls signup endpoint");
        if (bindingResult.hasErrors()) {
            List<String> e = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
        return ResponseEntity.ok(authenticationService.registerUser(userRegisterDto,httpServletResponse));
    }

    /**
     * Endpoint for refreshing the access token. This endpoint returns a new access token.
     *
     * @param authorizationHeader The authorization header
     * @return The new access token
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Access token refreshed successfully"),
            @ApiResponse(code = 401, message = "Unauthorized, invalid refresh token")
    })
    @Operation(summary = "Refresh the access token",
            description = """
                    Refreshes the access token using the provided refresh token. Send the refresh token as an
                    Authorization header. The new access token is returned in the response body.
                    """)
    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> getAccessTokenFromRefreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authenticationService.getAccessTokenFromRefreshToken(authorizationHeader));
    }
}