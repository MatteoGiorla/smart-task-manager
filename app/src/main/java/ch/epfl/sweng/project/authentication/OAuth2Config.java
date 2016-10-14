package ch.epfl.sweng.project.authentication;

/**
 * Configuration of OAuth2 client
 */

/**
 * taken from tequila-sample: to see how that works!
 */
public final class OAuth2Config {
    public final String[] scopes;
    public final String clientId;
    public final String clientSecret;
    public final String uri;

    public OAuth2Config(String[] scopes, String clientId, String clientSecret, String uri) {
        this.scopes = scopes;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.uri = uri;
    }
}
