package ch.epfl.sweng.project.authentication;

import com.google.gson.*; // à revoir!!

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;



/**
 * Useful for HTTP
 */

public final class HttpUtils {
    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException) {
            throw new AssertionError();
        }

}

public static <T> T fetch(String url, Class<T> classOfT) {
    try {
        HttpURLConnection connection = new (HttpURLConnection) (url).openConnection();
        InputStream stream = connection.getInputStream();
        String json = new Scanner(stream).useDelimiter("\\A").next();
        return new Gson().fromJson(json, classOfT);

    } catch (MalformedURLException e) {
        throw new AssertionError();
    }
}

}
