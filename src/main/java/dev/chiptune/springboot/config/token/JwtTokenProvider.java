package dev.chiptune.springboot.config.token;

import dev.chiptune.springboot.config.domain.CustomUserDetails;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {

    private final static Logger logger = Logger.getLogger(JwtTokenProvider.class.getName());

    private final String accessTokenKey = "sample-access-key";

    private final String refreshTokenKey = "sample-refresh-key";

    private final int accessTokenExpirationTime = 900000; // 액세스 토큰의 유효 시간 (예: 15분)

    private final int refreshTokenExpirationTime = 1800000; // 리프레시 토큰의 유효 시간 (예: 7일)

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 액세스 토큰 생성
    public String generateAccessToken(CustomUserDetails userDetails) {
        logger.info("━━━━━━━━━━━━━━━━━generateAccessToken START━☂☄");
        logger.info("☆ﾟ.*･｡ﾟ 액세스 토큰 만료 시간 " + accessTokenExpirationTime + " milliseconds ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("password", userDetails.getPassword());
        return new String(Base64.getEncoder().encode(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(signatureAlgorithm, accessTokenKey)
                .compact().getBytes(StandardCharsets.UTF_8)));
    }

    // 구 액세스 토큰으로 신규 액세스 토큰 발급
    public String generateNewAccessToken(String accessToken) {
        logger.info("━━━━━━━━━━━━━━━━━generateNewAccessToken START━☂☄");
        logger.info("☆ﾟ.*･｡ﾟ 액세스 토큰 만료 시간 " + accessTokenExpirationTime + " milliseconds ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", getUsernameFromToken(accessToken));
        claims.put("password", getPasswordFromToken(accessToken));

        return new String(Base64.getEncoder().encode(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(signatureAlgorithm, accessTokenKey)
                .compact().getBytes(StandardCharsets.UTF_8)));
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken() {
        logger.info("━━━━━━━━━━━━━━━━━generateRefreshToken START━☂☄");
        return new String(Base64.getEncoder().encode(Jwts.builder()
                .setClaims(null)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(signatureAlgorithm, refreshTokenKey)
                .compact().getBytes(StandardCharsets.UTF_8)));
    }

    // 토큰의 유효성 검증
    public boolean validateAccessToken(String token) {
        logger.info("━━━━━━━━━━━━━━━━━validateAccessToken START━☂☄");
        try {
            String decryptedToken = new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(accessTokenKey)
                    .parseClaimsJws(decryptedToken);

            logger.info("Token Claims Expiration");
            logger.info(claims.getBody().getExpiration().toString());
            logger.info(new Date().toString());

            logger.info("━━━━━━━━━━━━━━━━━validateAccessToken END━☂☄");
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

    }

    // 리프레시 토큰 유효성 검증
    public boolean validateRefreshToken(String accessToken, String refreshToken) {
        logger.info("━━━━━━━━━━━━━━━━━validateRefreshToken START━☂☄");
        try {
            String decryptedToken = new String(Base64.getDecoder().decode(refreshToken.getBytes(StandardCharsets.UTF_8)));
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(refreshTokenKey)
                    .parseClaimsJws(decryptedToken);

            logger.info("Token Claims Expiration");
            logger.info(claims.getBody().getExpiration().toString());
            logger.info(new Date().toString());

            logger.info("━━━━━━━━━━━━━━━━━validateRefreshToken END━☂☄");
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Claims extractClaims(String token) {
        String baseDecryptedToken = new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
        return Jwts.parser().setSigningKey(accessTokenKey).parseClaimsJws(baseDecryptedToken).getBody();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("username", String.class);
    }

    public String getPasswordFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("password", String.class);
    }

    // 요청 헤더에서 JWT 토큰을 추출하는 메소드입니다.
    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // "Authorization" 헤더가 "Bearer "로 시작하는 경우, 토큰 부분을 추출합니다.
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 문자열(토큰)을 반환합니다.
        }
        return null; // 조건에 맞지 않는 경우 null을 반환합니다.
    }
}
