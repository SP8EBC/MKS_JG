package pl.jeleniagora.mks.online;

import java.nio.charset.Charset;
import java.util.Base64;

import org.springframework.http.HttpHeaders;

public class HttpSimpleAuthHeader {

	public static HttpHeaders create(String username, String password) {
		HttpHeaders out = new HttpHeaders();
		String auth = username + ":" + password;
		
        byte[] encodedAuth = Base64.getEncoder().encode( 
                auth.getBytes(Charset.forName("US-ASCII")) );
		
        String authHeader = "Basic " + new String (encodedAuth);
        
        out.set("Authorization", authHeader);
        
		return out;
		
	}
}
