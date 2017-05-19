package uk.ac.aston.cs.forgetmenot.Controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.ac.aston.cs.forgetmenot.R;


/**
 * Created by Dhanishrao_2 on 28/01/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener, LocationListener {

    GoogleMap myGoogleMap;
    MapView mapView;
    View myView;
    private LocationManager locationManager;

    private GoogleApiClient mGoogleApiClient;
    private Double lat;
    private Double lon;
    private Location mLastLocation;
    private MapFragmentInterface sendLocation = null;
    private LatLng currentLocation;
    private Marker currentMarker;
    private boolean canPressNextBool = false;
    public Location testLocation;

    private LocationRequest mLocationRequest;
    private Location fusedLocation;

    @Override
    public void onLocationChanged(Location location) {
        testLocation = location;

    }


    /**
     * Sends the location of the item to the Record Activity via the Interface
     */
    public interface MapFragmentInterface {
        public void setLocation(Location location);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.map_fragment, container, false);
        ((RecordActivity) getActivity()).disableButton(true, true, false, true, 250, 250, 150, 250);
        Button next = (Button) myView.findViewById(R.id.next_button_3);

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PreviewFragment previewFragment = new PreviewFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, previewFragment)
                        .addToBackStack(null)
                        .commit();

            }

        });

        Button cancel = (Button) myView.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        ((RecordActivity) getActivity()).loadMainMenuActivity();
                    }
                });

        return myView;
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
     * Creates the GoogleApiClient for connecting to the services
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

        myGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        canPressNextBool = true;
       sendLocation.setLocation(mLastLocation);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
               != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        fusedLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (fusedLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {

            setLatLng(fusedLocation);
            currentLocation = new LatLng(fusedLocation.getLatitude(), fusedLocation.getLongitude());
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 20));
            createMarker();
            mLastLocation = fusedLocation;
            setLatLng(fusedLocation);
            currentLocation = new LatLng(fusedLocation.getLatitude(), fusedLocation.getLongitude());
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 20));

        }


    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position = marker.getPosition();
        setLocationOnMarkerDrag(position);


    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position = marker.getPosition();
        setLocationOnMarkerDrag(position);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position = marker.getPosition();
        setLocationOnMarkerDrag(position);
    }


    /**
     * creates the marker for where the item is at
     */
    public void createMarker() {
        if (currentMarker != null) {
            currentMarker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions().position(currentLocation).draggable(true);
        myGoogleMap.setOnMarkerDragListener(this);
        currentMarker = myGoogleMap.addMarker(markerOptions);
    }


    /**
     * Sets the latitude and longitude to the appropriate fields based on the item location provided
     * @param location Location of item
     */
    public void setLatLng(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    /**
     * Sets the latitude and longitude to the where the marker has been dragged on the screen
     * @param latLng LatLng of the marker
     */
    public void setLocationOnMarkerDrag(LatLng latLng) {
        mLastLocation.setLatitude(latLng.latitude);
        mLastLocation.setLongitude(latLng.longitude);
        sendLocation.setLocation(mLastLocation);
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
            sendLocation = (MapFragmentInterface) context;
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
        sendLocation.setLocation(mLastLocation);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
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

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();

    }





}



