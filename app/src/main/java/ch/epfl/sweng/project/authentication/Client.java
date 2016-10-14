package ch.epfl.sweng.project.authentication;

/**
 * Client code for Tequila authentication.
 */

public final class Client {
    public static String createRequestUrl (OAuth2Config config) {
        return "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/auth" +
                "?response_type=code" +
                "&client_id=" + HttpUtils.urlEncode(config.clientId) +
                "&redirect_uri=" + HttpUtils.urlEncode(config.clientId) +
                "&redirect_uri=" + HtppUtils.urlEncode(config.redirectUri) +
                "&scope=" + String.join(",", config.scopes);
    }

    public static String extractCode(String uri) {
        String marker = "code=";
        return uri.substring(uri.indexOf(marker) + marker.length());
    }
}
