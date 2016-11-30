package ch.epfl.sweng.project;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests!
 */
@RunWith(AndroidJUnit4.class)
public class GeolocationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    // TODO enable this:
    /**
     * Test showing use of mock location data to test code using the Google Play Location APIs. To
     * run this test, you must first check the "Allow mock locations" setting within
     * Settings -> Developer options.
     */
    /**
     * The name of the mock location.
     */



    



   /* private static final String NORTH_POLE = "ch.epfl.sweng.project" + ".NORTH_POLE";

    private static final float NORTH_POLE_LATITUDE = 90.0f;

    private static final float NORTH_POLE_LONGITUDE = 0.0f;

    private static final float ACCURACY_IN_METERS = 10.0f;

    private static final int AWAIT_TIMEOUT_IN_MILLISECONDS = 2000;

    private static final String TAG = "Geolocation Test";

    private MainActivity mMainActivity;

    public GeolocationTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        ensureGoogleApiClientConnection();
    }

    public void testUsingMockLocation() {
        setMockLocation(createNorthPoleLocation());

        confirmPreconditions();

        clickStartUpdatesButton();

        final android.location.Location testLocation = createNorthPoleLocation();

        assertEquals(testLocation.getLatitude(), mMainActivity.mCurrentLocation.getLatitude(), 0.000001f);
        assertEquals(testLocation.getLongitude(), mMainActivity.mCurrentLocation.getLongitude(), 0.000001f);
    }

    private void ensureGoogleApiClientConnection() {
        if (!mMainActivity.mGoogleApiClient.isConnected()) {
            mMainActivity.mGoogleApiClient.blockingConnect();
        }
    }

    private void confirmPreconditions() {
        assertNotNull("mMainActivity is null", mMainActivity);
        assertTrue("GoogleApiClient is not connected", mMainActivity.mGoogleApiClient.isConnected());
    }

    private void clickStartUpdatesButton() {
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainActivity.onConnected(Bundle.EMPTY); // TODO pas sûr sûr
            }
        });
    }

    private void setMockLocation(final android.location.Location mockLocation) {
        final CountDownLatch lock = new CountDownLatch(1);

        LocationServices.FusedLocationApi.setMockMode(mMainActivity.mGoogleApiClient, true)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            if (ActivityCompat.checkSelfPermission(mMainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mMainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.

                            }
                            LocationServices.FusedLocationApi.setMockLocation(
                                    mMainActivity.mGoogleApiClient,
                                    mockLocation
                            ).setResultCallback(new ResultCallback<Status>() {
                                @Override
                                public void onResult(@NonNull Status status) {
                                    if (status.isSuccess()) {
                                        lock.countDown();
                                    } else {
                                        Log.e(TAG, "Mock location not set");
                                    }
                                }
                            });
                        } else {
                            Log.e(TAG, "Mock mode not set");
                        }
                    }
                });
        try {
            lock.await(AWAIT_TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            Log.i(TAG, "Waiting thread awakened prematurely", exception);
        }
    }

    private android.location.Location createNorthPoleLocation() {
        android.location.Location mockLocation = new android.location.Location(NORTH_POLE);
        mockLocation.setLatitude(NORTH_POLE_LATITUDE);
        mockLocation.setLongitude(NORTH_POLE_LONGITUDE);
        mockLocation.setAccuracy(ACCURACY_IN_METERS);
        mockLocation.setTime(System.currentTimeMillis());
        return mockLocation;
    }*/
}