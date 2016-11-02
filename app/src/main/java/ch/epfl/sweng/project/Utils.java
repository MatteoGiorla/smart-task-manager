package ch.epfl.sweng.project;


public class Utils {

    /**
     * Encode a given mail to be compatible with keys in firebase
     *
     * @param mail The user email
     * @return The encoded email
     */
    public static String encodeMailAsFirebaseKey(String mail) {
        return mail.replace('.', ' ');
    }
}
