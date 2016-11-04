package ch.epfl.sweng.project;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


    public static void addUser(User user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child("users").child(Utils.encodeMailAsFirebaseKey(user.getEmail())).getRef();
        userRef.setValue(user);
    }

    public static void updateUser(User user) {
        deleteUser(user);
        addUser(user);
    }

    /**
     * Deleter a user in the database
     *
     * @param user The user to be deleted
     */
    private static void deleteUser(User user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child("users").child(Utils.encodeMailAsFirebaseKey(user.getEmail())).getRef();
        userRef.removeValue();
    }
}
