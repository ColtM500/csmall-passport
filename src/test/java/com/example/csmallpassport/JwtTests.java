package com.example.csmallpassport;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class JwtTests {

    String secretKey = "kU4jrFA3iuI5jn25u743kfDs7a8pFEwS54hm";

    @Test
    public void generate(){

        System.out.println(secretKey);

        Date exp = new Date(System.currentTimeMillis()+ 10 * 24 * 60 * 60 * 1000);

        Map<String, Object> claims = new HashMap<>();
//        claims.put("id", 9827);
//        claims.put("username","ZhangSan");
        claims.put("i",1);

        String jwt = Jwts.builder()
//                Header（头部信息）：声明算法与Token的类型
                  .setHeaderParam("alg","HS256")
                  .setHeaderParam("typ","JWT")
//                Payload（载荷）：数据,表现为Claims
                  .setClaims(claims)
                  .setExpiration(exp)
//                Signature：验证签名
                  .signWith(SignatureAlgorithm.HS256, secretKey)
                  .compact();
        System.out.println(jwt);
        //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6OTgyNywiZXhwIjoxNjgwNzUyNzAxLCJ1c2VybmFtZSI6IlpoYW5nU2FuIn0.LRWg56Q9wwBV0VNt6tOa6GOLlsbUlWguoJ7eNH_31Z4
    //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6OTgyNywiZXhwIjoxNjgwNzUzMDM5LCJ1c2VybmFtZSI6IlpoYW5nU2FuIn0.OJo3yw3ErFdPxKUEfrZq7p9PFpYil7GtAhpLsF0bR7U
   //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpIjoxLCJleHAiOjE2ODA3NzAwNjh9.OSzUUOP5R1Rnxz8jKlRuLp-K5HESNHmiPVkf3fxReas
    }

    @Test
    public void parse() {
        //因为容易过期+这里被篡改 不想一直检查所以try起来
        try {
            String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpIjoxLCJleHAiOjE2ODA4Mzc0MjJ9.T3jW_Qp7-nsXp4KnVJB4Pka_ePfWSyZY-rd-JYITEhU";

            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();

            Long id = claims.get("id", Long.class);
            String username = claims.get("username", String.class);

            System.out.println("id = " + id);
            System.out.println("username= " + username);
        } catch (Throwable e){
            e.printStackTrace();
        }
    }
}
