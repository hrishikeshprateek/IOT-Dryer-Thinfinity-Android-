package thundersharp.thinkfinity.dryer;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class JSONUtils {

    public static JSONUtils getInstance(){
        return new JSONUtils();
    }

    public static Map<String, Object> extractClaimsFromToken(String jwtToken) {
        try {
            // Extract the payload section of the JWT token (the middle part between dots)
            String[] parts = jwtToken.split("\\.");
            String base64EncodedPayload = parts[1];

            // Decode the base64-encoded payload
            byte[] decodedPayload = Base64.getDecoder().decode(base64EncodedPayload);

            // Convert the decoded payload bytes to a JSON string
            String payloadJson = new String(decodedPayload, StandardCharsets.UTF_8);

            // Parse the JSON string into a Map representing the claims
            @SuppressWarnings("unchecked")
            Map<String, Object> claims = new ObjectMapper().readValue(payloadJson, Map.class);

            return claims; // Return the extracted claims

        } catch (Exception e) {
            System.err.println("Error extracting JWT claims: " + e.getMessage());
            return null;
        }
    }

}
