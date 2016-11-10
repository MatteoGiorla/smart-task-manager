package ch.epfl.sweng.project.data;

/**
 * Class that decide which provider the app use in
 * order to manipulate users in the database
 */
public class UserProvider {
    public static final String FIREBASE_PROVIDER = "Firebase";
    public static final String TEST_PROVIDER = "Tests";
    private static String mProvider = FIREBASE_PROVIDER;

    /**
     * Getter that return the Proxy to reach
     * the database
     * @return UserHelper, the proxy
     */
    public UserHelper getUserProvider() {
        switch (mProvider) {
            case FIREBASE_PROVIDER:
                return new FirebaseUserHelper();
            case TEST_PROVIDER:
                return new LocalUserHelper();
            default:
                throw new IllegalArgumentException("This provider does not exists !");
        }
    }

    /**
     * Setter that allow to switch between Providers
     * @param provider should be UserProvider.FIREBASE_PROVIDER or
     *                 UserProvider.TEST_PROVIDER
     * @throws IllegalArgumentException if the given provider is unknown
     */
    public static void setProvider(String provider) {
        if(!provider.equals(TEST_PROVIDER) || !provider.equals(FIREBASE_PROVIDER)) {
            throw new IllegalArgumentException("Unexpected provider !");
        }
        mProvider = provider;
    }
}
