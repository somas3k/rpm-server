package pl.edu.agh.im.remotepatientmonitor.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

import static pl.edu.agh.im.remotepatientmonitor.auth.SecurityConstants.*;

@Service
public class JWTService {
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String createSignedEmailToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setHeaderParams(Collections.singletonMap("type", "email"))
                .setExpiration(new Date(System.currentTimeMillis() + MAIL_ACTIVATION_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
    }

    public String getTypeFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getHeader()
                .get("type")
                .toString();
    }

    public String createSignedLoginToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
    }

//    public String getGuestTokenAfterLogout() {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("role", "guest");
//        return Jwts.builder()
//                .setClaims(payload)
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
//                .compact();
//    }
}
