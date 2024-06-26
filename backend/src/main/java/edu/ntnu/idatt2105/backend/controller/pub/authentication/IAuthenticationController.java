package edu.ntnu.idatt2105.backend.controller.pub.authentication;

import edu.ntnu.idatt2105.backend.dto.security.AuthenticationResponseDTO;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * This interface contains the methods of the AuthenticationController.
 *
 * @author Trym Hamer Gudvangen, Brage Halvorsen Kvamme
 * @version 1.1 05.04.2024
 */
public interface IAuthenticationController {

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
    ResponseEntity<AuthenticationResponseDTO> login(
            Authentication authentication, HttpServletResponse httpServletResponse
    );

    /**
     * Endpoint for signing up. This endpoint registers a new user and returns the access and refresh token.
     * The access token is returned in the response body and the refresh token is returned as a cookie.
     *
     * @param username The username
     * @param password The password
     * @param email The email
     * @param imageFile The image file
     * @param httpServletResponse The http response
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
    @PostMapping(value = "/signup",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ResponseEntity<AuthenticationResponseDTO> signup(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam(name = "image", required = false) MultipartFile imageFile,
            HttpServletResponse httpServletResponse
    ) throws IOException;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User registered successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @Operation(summary = "Register a new user",
            description = """
                    Registers a new user and returns the access and refresh tokens. The access token is returned in the response body, and the refresh token is returned as a cookie. Provide user registration details in the request body.
                    """)
    @GetMapping(value = "/signup")
    ResponseEntity<AuthenticationResponseDTO> signup(
            @Nullable Long userId,
            HttpServletResponse httpServletResponse
    );

    /**
     * Endpoint for refreshing the access token. This endpoint returns a new access token.
     *
     * @param refreshToken The refreshToken
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
    //@PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token/get")
    ResponseEntity<AuthenticationResponseDTO> getAccessTokenFromRefreshToken(
            @CookieValue(value = "refresh_token", defaultValue = "") String refreshToken
    );
}
