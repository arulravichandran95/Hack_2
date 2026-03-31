package com.example.Leave_Backend.security; // Declares this class belongs to the security package

import com.example.Leave_Backend.model.entity.User; // Imports the User entity to fetch user details from DB
import com.example.Leave_Backend.repository.UserRepository; // Imports UserRepository to query users by email
import io.jsonwebtoken.*; // Imports all JJWT classes needed for building and parsing JWT tokens
import io.jsonwebtoken.security.Keys; // Imports the Keys utility for generating HMAC signing keys
import jakarta.annotation.PostConstruct; // Imports annotation to run initialization logic after bean creation
import org.springframework.beans.factory.annotation.Autowired; // Imports Autowired for dependency injection
import org.springframework.beans.factory.annotation.Value; // Imports Value to read config from application.properties
import org.springframework.security.core.Authentication; // Imports Authentication object holding current user's security context
import org.springframework.security.core.userdetails.UserDetails; // Imports UserDetails interface used by Spring Security
import org.springframework.stereotype.Component; // Marks this class as a Spring-managed component bean

import java.security.Key; // Imports the Key interface used as the JWT signing key
import java.util.Date; // Imports Date for setting issued-at and expiration timestamps
import java.util.HashMap; // Imports HashMap to hold JWT claims key-value pairs
import java.util.Map; // Imports Map interface used for claims
import java.util.function.Function; // Imports Function interface for extracting specific claims generically

@Component // Registers this class as a Spring bean so it can be injected anywhere in the application
public class JwtTokenProvider { // Class responsible for creating, validating, and parsing JWT tokens

    @Value("${app.jwt.secret}") // Reads the JWT secret key from application.properties/yml
    private String jwtSecret; // Holds the secret string used to sign JWT tokens

    @Value("${app.jwt.expiration-ms}") // Reads the token expiration time (in milliseconds) from config
    private long jwtExpirationMs; // Duration (ms) after which a JWT token will expire

    @Autowired // Injects the UserRepository bean automatically
    private UserRepository userRepository; // Used to look up the full User object by email when generating a token

    private Key key; // Holds the cryptographic key used for signing and verifying JWTs

    @PostConstruct // Runs this method automatically after the bean is initialized and all fields are injected
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes()); // Converts the secret string into a secure HMAC-SHA key
    }

    public String generateToken(Authentication authentication) { // Generates a JWT token for an authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // Extracts the UserDetails from the authentication object
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(); // Fetches the full User entity from DB using email as username

        Map<String, Object> claims = new HashMap<>(); // Creates a map to hold custom claims embedded in the token
        claims.put("userId", user.getId()); // Embeds the user's database ID into the token payload
        claims.put("email", user.getEmail()); // Embeds the user's email into the token payload
        claims.put("role", user.getRole().name()); // Embeds the user's role (ADMIN/EMPLOYEE/MANAGER) into the token

        return Jwts.builder() // Starts building the JWT token
                .setClaims(claims) // Sets all the custom claims into the token body
                .setSubject(userDetails.getUsername()) // Sets the token subject to the user's email (standard JWT field)
                .setIssuedAt(new Date()) // Sets the current timestamp as the token's issue time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Sets the expiry date based on config
                .signWith(key, SignatureAlgorithm.HS256) // Signs the token using HMAC-SHA256 algorithm with our secret key
                .compact(); // Builds and returns the final serialized JWT string
    }

    public String getUsernameFromToken(String token) { // Extracts the subject (email/username) from a given JWT token
        return getClaimFromToken(token, Claims::getSubject); // Uses the generic extractor to get the 'sub' claim
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) { // Generic method to extract any single claim from the token
        final Claims claims = getAllClaimsFromToken(token); // First parses the full claims from the token
        return claimsResolver.apply(claims); // Applies the provided function to extract the desired claim value
    }

    private Claims getAllClaimsFromToken(String token) { // Parses and returns all claims body from a JWT token string
        return Jwts.parserBuilder() // Creates a new JWT parser builder
                .setSigningKey(key) // Sets the key to verify the token's signature
                .build() // Builds the parser
                .parseClaimsJws(token) // Parses and validates the signed JWT; throws exception if invalid/expired
                .getBody(); // Returns the claims payload (the body of the token)
    }

    public boolean validateToken(String token) { // Validates if a JWT token is well-formed, signed correctly, and not expired
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // Parses the token; throws exception if invalid
            return true; // Token is valid — return true
        } catch (JwtException | IllegalArgumentException e) { // Catches expired, malformed, unsupported, or null tokens
            return false; // Token is invalid — return false (do not throw; just deny)
        }
    }
}
