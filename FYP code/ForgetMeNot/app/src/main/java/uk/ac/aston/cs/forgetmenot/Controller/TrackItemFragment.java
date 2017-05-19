package uk.ac.aston.cs.forgetmenot.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;

import static com.google.maps.android.SphericalUtil.computeHeading;

//import android.location.LocationListener;

/**
 * Created by Dhanishrao_2 on 28/01/2017.
 */

public class TrackItemFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap myGoogleMap;
    private MapView mapView;
    private View myView;
    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private Double lat;
    private Double lon;
    private Location mLastLocation;
    private LatLng currentLocation;
    private Marker currentMarker;
    private boolean canPressNextBool = false;
    private DatabaseHelper dbHelper;
    private List<Item> items;
    private LocationRequest mLocationRequest;
    private Location fusedLocation;
    private Marker currentItemMarker;
    private boolean createdFromMenu;
    private String name;
    private double latitude;
    private double longitude;
    private Location itemLocation;
    private float bearingF;
    private Vibrator v;
    private boolean startVibrating;
    private Location testOldLocation;
    private double directDegree;
    private long id;
    private LatLng oldlocation = null;
    private Button startVibratingButton;
    private Button stopVibratingButton;


    @Override
    public void onLocationChanged(Location location) {
        fusedLocation = location;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (testOldLocation == null) {
            testOldLocation = location;
        } else {
            if (testOldLocation.bearingTo(fusedLocation) < 0) {
                directDegree = testOldLocation.bearingTo(fusedLocation) + 360;
                testOldLocation = location;
            } else {
                directDegree = testOldLocation.bearingTo(fusedLocation);
                testOldLocation = location;
            }
        }


        if (fusedLocation.bearingTo(itemLocation) < 0) {
            bearingF = fusedLocation.bearingTo(itemLocation) + 360;
            startVibratonAi(bearingF);
        } else {
            bearingF = fusedLocation.bearingTo(itemLocation);
            startVibratonAi(bearingF);
        }


        if (startVibrating == false) {
            if (oldlocation == null) {
                oldlocation = new LatLng(location.getLatitude(), location.getLatitude());


            } else {
                LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (computeHeading(oldlocation, newLocation) < 0) {
                    double degrees = computeHeading(oldlocation, new LatLng(location.getLatitude(), location.getLongitude())) + 360;

                    oldlocation = newLocation;
                } else {
                    double degrees = computeHeading(oldlocation, newLocation);

                    oldlocation = newLocation;
                }
            }
        }


    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createdFromMenu = false;
        dbHelper = new DatabaseHelper(getContext());
        buildGoogleApiClient();
        startVibrating = true;
        v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {

            this.id = bundle2.getLong("Id");
            this.latitude = bundle2.getDouble("Latitude");
            this.longitude = bundle2.getDouble("Longitude");
            this.name = bundle2.getString("Name");
            this.createdFromMenu = bundle2.getBoolean("createdFromMenu");
            itemLocation = new Location("");
            itemLocation.setLatitude(latitude);
            itemLocation.setLongitude(longitude);
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.track_item_fragment, container, false);
        Button cancel = (Button) myView.findViewById(R.id.back_button);
        Button delete = (Button) myView.findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteItem(id);
                if (createdFromMenu) {
                    Intent MenuActivityIntent = new Intent(getActivity(), MainMenuActivity.class);
                    startActivity(MenuActivityIntent);
                }else {
                    Intent findActivityIntent = new Intent(getActivity(), FindActivity.class);
                    startActivity(findActivityIntent);
                }
            }
        });
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if (createdFromMenu) {
                            Intent MenuActivityIntent = new Intent(getActivity(), MainMenuActivity.class);
                            startActivity(MenuActivityIntent);
                        } else {
                            getFragmentManager().popBackStack();
                        }
                    }
                });
        startVibratingButton = (Button) myView.findViewById(R.id.start_vibrating);
        stopVibratingButton = (Button) myView.findViewById(R.id.stop_vibrating);
        startVibratingButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        startVibrating = true;
                        disableButton(true, false);
                    }
                });

        stopVibratingButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {


                        startVibrating = false;
                        disableButton(false, false);
                    }
                });


        disableButton(true, false);

        return myView;
    }

    /**
     * Disables and changes the colour of the vibration button based on which icon has been pressed
     * @param vibrate Boolean of whether the vibrate button has been pressed
     * @param normal Boolean of whether the not vibrate button has neem pressed
     */
    public void disableButton(boolean vibrate, boolean normal) {
        if (normal) {
            startVibratingButton.setBackgroundResource(R.drawable.state_vibrating_button);
            stopVibratingButton.setBackgroundResource(R.drawable.state_not_vibrate_button);
        } else {
            if (vibrate) {

                startVibratingButton.setBackgroundResource(R.drawable.pressed_vibrating_button);
                stopVibratingButton.setBackgroundResource(R.drawable.state_not_vibrate_button);
            } else {
                startVibratingButton.setBackgroundResource(R.drawable.state_vibrating_button);
                stopVibratingButton.setBackgroundResource(R.drawable.pressed_not_vibrate_button);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) myView.findViewById(R.id.map_container);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }


    }

    /**
     * Responsible for initialising the Google api client
     */
    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        myGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        myGoogleMap.setMyLocationEnabled(true);
        myGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        canPressNextBool = true;


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3500); // Update location every second

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        fusedLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (fusedLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            setLatLng(fusedLocation);
            currentLocation = new LatLng(fusedLocation.getLatitude(), fusedLocation.getLongitude());
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            builder.include(new LatLng(fusedLocation.getLatitude(), fusedLocation.getLongitude()));
            builder.include(new LatLng(latitude, longitude));

            LatLngBounds bounds = builder.build();
            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 420));

            createItemMarker();

            mLastLocation = fusedLocation;
            setLatLng(fusedLocation);
            LatLng itemLocation = new LatLng(latitude, longitude);
            MarkerOptions markerOptions2 = new MarkerOptions().position(itemLocation).title(name);
            currentItemMarker = myGoogleMap.addMarker(markerOptions2);

        }

    }

    /**
     * Creates the marker where the item is at
     */
    public void createItemMarker() {

        LatLng itemLocation = new LatLng(latitude, longitude);
        MarkerOptions markerOptions2 = new MarkerOptions().position(itemLocation);

        currentItemMarker = myGoogleMap.addMarker(markerOptions2);
    }


    public void setLatLng(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }


    @Override
    public void onConnectionSuspended(int i) {
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onPause() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        v.cancel();
        super.onPause();

        disableButton(false, true);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    //Allows for the client to be disconnected when the fragment is stopped
    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    /**
     * Creates the various the vibrational pattern depending on where the user is facing compared to where the item is at
     * @param currentBearing bearing of the user to the item
     */
    public void startVibratonAi(float currentBearing) {
        long[] patternForward = {0, 100, 800, 100, 1000};
        long[] patternLeft = {0, 800, 800, 100, 1000};
        long[] patternFurtherLeft = {0, 1200, 800, 100, 1000};
        long[] patternRight = {0, 100, 800, 800, 1200};
        long[] patternFurtherRight = {0, 100, 800, 1200, 1000};
        long[] patternBack = {0, 100, 500, 100, 500, 100, 1000};
        long[] patternFound = {0, 10000};

        if (startVibrating == false) {
            v.cancel();

            return;
        } else {
            if (calculateDistanceBetween() <= 2) {
                v.vibrate(patternFound, 0);

            } else if (currentBearing == getRecalculatedDegrees(0)) {
                v.vibrate(patternForward, 0);

            } else if (currentBearing > getRecalculatedDegrees(314) && currentBearing < getRecalculatedDegrees(360)) {
                v.vibrate(patternLeft, 0);

            } else if (currentBearing > getRecalculatedDegrees(270) && currentBearing <= getRecalculatedDegrees(314)) {
                v.vibrate(patternFurtherLeft, 0);

            } else if (currentBearing > getRecalculatedDegrees(0) && currentBearing < getRecalculatedDegrees(45)) {
                v.vibrate(patternRight, 0);

            } else if (currentBearing >= getRecalculatedDegrees(45) && currentBearing < getRecalculatedDegrees(90)) {
                v.vibrate(patternFurtherRight, 0);

            } else if (currentBearing <= getRecalculatedDegrees(270) && currentBearing >= getRecalculatedDegrees(90)) {
                v.vibrate(patternBack, 0);


            }

        }

    }

    /**
     * changes the degree based on the users previous location and current location
     * @param degree Double degree
     * @return degree of type Double
     */
    private double getRecalculatedDegrees(double degree) {
        double degreeCalc = (degree + directDegree) % 360;
        return degreeCalc;
    }

    /**
     * Calculates the distance between the user and the item
     * @return distance of type Float
     */
    private float calculateDistanceBetween() {
        return fusedLocation.distanceTo(itemLocation);
    }


}



