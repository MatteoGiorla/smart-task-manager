package ch.epfl.sweng.project.data;


public class UserProvider {
    public static final String FIREBASE_PROVIDER = "Firebase";
    public static final String TEST_PROVIDER = "Tests";
    private static String mProvider = FIREBASE_PROVIDER;

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

    public static void setProvider(String provider) {
        mProvider = provider;
    }
}
