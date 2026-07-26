package ec.edu.monster.security;

import ec.edu.monster.exception.AccountException;
import jakarta.json.Json;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class JwtUtil {
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final long TOKEN_TTL_SECONDS = 3600;
    private static final String SECRET = System.getenv().getOrDefault("JWT_SECRET", "change-me");

    private JwtUtil() {}

    public static JwtPayload validateToken(String token) {
        try {
            String normalized = normalizeToken(token);
            if (isBlank(normalized)) throw new AccountException("Invalid token", 400);
            String[] parts = normalized.split("\\.");
            if (parts.length != 3) throw new AccountException("Invalid token", 400);
            String signingInput = parts[0] + "." + parts[1];
            byte[] expectedSignature = hmacSha256(signingInput);
            byte[] actualSignature = base64UrlDecodeToBytes(parts[2]);
            if (!MessageDigest.isEqual(expectedSignature, actualSignature)) throw new AccountException("Invalid token", 400);
            JsonObject payload = parseJson(base64UrlDecode(parts[1]));
            JsonNumber expNumber = payload.getJsonNumber("exp");
            if (expNumber == null) throw new AccountException("Invalid token", 400);
            long exp = expNumber.longValue();
            if (Instant.now().getEpochSecond() >= exp) throw new AccountException("Token expired", 400);
            String username = payload.getString("sub", null);
            String role = payload.getString("role", null);
            if (username == null || role == null) throw new AccountException("Invalid token", 400);
            return new JwtPayload(username, role, exp);
        } catch (AccountException ex) { throw ex; }
        catch (Exception ex) { throw new AccountException("Invalid token", 400); }
    }

    private static byte[] hmacSha256(String input) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) { throw new IllegalStateException("Unable to sign token", ex); }
    }

    private static String base64UrlDecode(String v) { return new String(base64UrlDecodeToBytes(v), StandardCharsets.UTF_8); }
    private static byte[] base64UrlDecodeToBytes(String v) { return Base64.getUrlDecoder().decode(v); }
    private static JsonObject parseJson(String json) {
        try (JsonReader r = Json.createReader(new StringReader(json))) { return r.readObject(); }
    }
    private static String normalizeToken(String t) {
        if (t == null) return null;
        String s = t.trim();
        return s.regionMatches(true, 0, "Bearer ", 0, 7) ? s.substring(7).trim() : s;
    }
    private static boolean isBlank(String v) { return v == null || v.trim().isEmpty(); }
}
