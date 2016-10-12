package ch.epfl.sweng.project;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.client.*;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created on 07.10.16.
 */

public class Authentication {
    public static String authenticate(String username, String password) {
        // pas à moi:
        String sessionId = null;
        String token = null;

        token = getToken();
        validateToken(token, username, password);
        sessionId = retrieveSessionId(token);

        return sessionId;
    }

    private static String getToken() {

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        HttpGet get = new HttpGet(HttpFactory.getSwengLogin());
        String token = "";
            String response = SwengHttpClientFactory.getInstance().execute(get,
                    responseHandler);
            JSONObject responseJSON = new JSONObject(response);
            token = responseJSON.getString("token");

        return token;
    }

    }
}
