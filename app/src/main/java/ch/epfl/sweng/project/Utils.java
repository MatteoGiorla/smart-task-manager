package ch.epfl.sweng.project;


import android.content.res.Resources;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private Utils() {}

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

    public static final Map<Integer, String> DURATIONS_MAP;
    static {
        Map<Integer, String> durationMap = new HashMap<Integer, String>();
        durationMap.put(5, Resources.getSystem().getString(R.string.duration5m));
        durationMap.put(15, Resources.getSystem().getString(R.string.duration15m));
        durationMap.put(30, Resources.getSystem().getString(R.string.duration30m));
        durationMap.put(60, Resources.getSystem().getString(R.string.duration1h));
        durationMap.put(120, Resources.getSystem().getString(R.string.duration2h));
        durationMap.put(240, Resources.getSystem().getString(R.string.duration4h));
        durationMap.put(480, Resources.getSystem().getString(R.string.duration1d));
        durationMap.put(960, Resources.getSystem().getString(R.string.duration2d));
        durationMap.put(1920, Resources.getSystem().getString(R.string.duration4d));
        durationMap.put(2400, Resources.getSystem().getString(R.string.duration1w));
        durationMap.put(4800, Resources.getSystem().getString(R.string.duration2w));
        durationMap.put(9600, Resources.getSystem().getString(R.string.duration1m));
        DURATIONS_MAP = Collections.unmodifiableMap(durationMap);
    }

    public static final Map<Integer, String> FRACTIONS_MAP;
    static {
        Map<Integer, String> fractionMap = new HashMap<Integer, String>();
        fractionMap.put(15, Resources.getSystem().getString(R.string.duration15m));
        fractionMap.put(30, Resources.getSystem().getString(R.string.duration30m));
        fractionMap.put(60, Resources.getSystem().getString(R.string.duration1h));
        fractionMap.put(120, Resources.getSystem().getString(R.string.duration2h));
        fractionMap.put(240, Resources.getSystem().getString(R.string.duration4h));
        fractionMap.put(480, Resources.getSystem().getString(R.string.duration1d));
        FRACTIONS_MAP = Collections.unmodifiableMap(fractionMap);
    }

    public static final Map<Integer, String> ENERGY_MAP;
    static {
        Map<Integer, String> energyMap = new HashMap<Integer, String>();
        energyMap.put(0, Resources.getSystem().getString(R.string.low_energy));
        energyMap.put(1, Resources.getSystem().getString(R.string.normal_energy));
        energyMap.put(2, Resources.getSystem().getString(R.string.high_energy));
        ENERGY_MAP = Collections.unmodifiableMap(energyMap);
    }
}
