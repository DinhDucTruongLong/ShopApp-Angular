package com.project.shopapp.components;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoder;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.HashMap;


import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private long expiration; //save to an environment variable bien moi truong

    @Value("${jwt.secretKey}")
    private String secretKey;
    public String generateToken(com.project.shopapp.models.User user) throws Exception{
        // properties => claims
        Map<String, Object> claims = new HashMap<>();
        //this.generateSecretKey();
        claims.put("phoneNumber", user.getPhoneNumber());
        try{
            String token = Jwts.builder()
                    .setClaims(claims)// xem ben trong token chua chung cai gi
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e){
            //ban co the inject logger, thay vi dung System.out.println
           throw new InvalidParameterException("Cannot create jwt token, error"+e.getMessage());
          // return null;
        }
    }
    private Key getSignInkey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey.trim());
        return Keys.hmacShaKeyFor(bytes);
    }
    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256-nit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
    // dung trong noi bo
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims =  this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // check expiration het hanj chua
    public boolean isTokenExpired(String token) {
        Date expirationeDate = this.extractClaim(token, Claims::getExpiration);
        return expirationeDate.before(new Date());
    }

    public String extractPhoneNumber(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // ktr token con han khong va cos trong userdetail khon
    public boolean validateToken(String token, UserDetails userDetails)
    {
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }

}
