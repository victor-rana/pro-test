package blackflame.com.zymepro.util.jwt;

import blackflame.com.zymepro.BuildConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;


/**
 * Created by ashish on 15/05/18.
 */

public class KeyGenerator {

    public static String getKey(String client){
        String jwt=null;

        try {


            jwt = Jwts.builder()
                    .setSubject("users/TzMUocMF4p")
                    .setExpiration(new Date(System.currentTimeMillis()+60*1000*8))
                    .claim("client_id", client)
                    .claim("creation_time",System.currentTimeMillis() )
                    .signWith(
                            SignatureAlgorithm.HS256,
                            BuildConfig.CLIENTKEY.getBytes("UTF-8")
                    )
                    .compact();


        }catch (Exception ex){
            return  null;
        }

        return jwt;
    }

    //Sample method to validate and read the JWT
    public static void parseJWT(String jwt) {



        //This line will throw an exception if it is not a signed JWS (as expected)

      try {
          Jws<Claims> claims = Jwts.parser()
                  .setSigningKey(BuildConfig.CLIENTKEY.getBytes("UTF-8"))
                  .parseClaimsJws(jwt);
//          String scope = claims.getBody().get("client").toString();
//          Log.e(TAG, "parseJWT: "+ scope);
//          Log.e(TAG, "parseJWT: client"+scope.equals(CommonPreference.getInstance().getClientToken()));
//
//
//          Log.e(TAG, "parseJWT: time"+claims.getBody().get("time").toString());
         // assertEquals(scope, "self groups/admins");
      }catch (Exception ex){
          ex.printStackTrace();
      }
    }


}
