package uk.ac.aston.cs.forgetmenot.Controller;

import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 03/04/2017.
 */

public class DisplayItemDetailsForFindFragment extends Fragment implements OnMapReadyCallback {//ItemListAdapter.SendItemDetailsInterface,


    private View findPreviewView;
    private Button trackButton;
    private Button backButton;
    private String name;
    private String note;
    private String imagePath;
    private TextView itemNameTextView;
    private TextView itemNotesTextView;
    private TextView mapText;
    private double latitude;
    private double longitude;
    private ImageView previewImageView;
    private DatabaseHelper dbHelper;
    Uri imageUri;
    private MapView mapView;
    GoogleMap myGoogleMap;
    private FragmentTransaction fragmentTransaction;
    private Item item;
    private long id;
    private Button deleteButton;
    private boolean createdFromMenu;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng itemLocation = new LatLng(latitude, longitude);
        myGoogleMap = googleMap;
        myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, 20));
        myGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        myGoogleMap.addMarker(new MarkerOptions()
                .title("Item is located here")
                .position(itemLocation));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        item = new Item();
        dbHelper = new DatabaseHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        findPreviewView = inflater.inflate(R.layout.find_preview_fragment, container, false);
        trackButton = (Button) findPreviewView.findViewById(R.id.track_button);
        backButton = (Button) findPreviewView.findViewById(R.id.back_button);
        deleteButton = (Button) findPreviewView.findViewById(R.id.delete_button);
        previewImageView = (ImageView) findPreviewView.findViewById(R.id.image_view_2);
        setItem();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent findActivityIntent = new Intent(getActivity(), FindActivity.class);
                dbHelper.deleteItem(id);
                startActivity(findActivityIntent);

            }
        });

        //Sets the image based on the image saved, otherwise it sets it based on the name of the item
        if (imagePath != null) {
            imageUri = Uri.parse(imagePath);


            Matrix rotatePicture = new Matrix();
            rotatePicture.postRotate(90);

            previewImageView.setImageMatrix(rotatePicture);


            previewImageView.setImageURI(imageUri);
            previewImageView.setRotation(90);
        } else {
            previewImageView.setImageResource(item.setImageForMatchedItem(name));
        }

        itemNameTextView = (TextView) findPreviewView.findViewById(R.id.preview_item_name);
        itemNotesTextView = (TextView) findPreviewView.findViewById(R.id.preview_item_notes);

        if (name != null) {
            itemNameTextView.setText(name);
        } else {
            itemNameTextView.setText("Item name was not added");
        }

        if (note != null) {
            itemNotesTextView.setText(note);
        } else {
            itemNotesTextView.setText("Item note was not added");
        }

        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude == 0.0 && longitude == 0.0) {
                    Toast.makeText(getContext(), "Cannot track, no location was set", Toast.LENGTH_LONG).show();
                } else {

                    TrackItemFragment trackItemFragment = new TrackItemFragment();

                    fragmentTransaction.replace(R.id.find_frame_container, trackItemFragment)
                            .addToBackStack(null)
                            .commit();
                    Bundle bundleDetails2 = new Bundle();
                    bundleDetails2.putLong("Id", id);
                    bundleDetails2.putString("Name", name);
                    bundleDetails2.putDouble("Latitude", latitude);
                    bundleDetails2.putDouble("Longitude", longitude);
                    trackItemFragment.setArguments(bundleDetails2);
                }


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createdFromMenu) {
                    Intent mainMenuActivity = new Intent(getActivity(), MainMenuActivity.class);

                    startActivity(mainMenuActivity);
                } else {
                    Intent findActivityIntent = new Intent(getActivity(), FindActivity.class);

                    startActivity(findActivityIntent);
                }
            }
        });
        return findPreviewView;
    }

    /**
     * Allocates the different fields with their content based on information sent through the bundle
     */
    public void setItem() {


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.name = bundle.getString("Name");
            this.note = bundle.getString("Note");
            this.id = bundle.getLong("Id", id);
            this.imagePath = bundle.getString("ImagePath");

            this.latitude = bundle.getDouble("Latitude");

            this.longitude = bundle.getDouble("Longitude");
            this.createdFromMenu = bundle.getBoolean("CreatedFromMenu");

        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!(latitude == 0.0 && longitude == 0.0)) {
            mapView = (MapView) findPreviewView.findViewById(R.id.preview_map_container);
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
        } else {
            mapText = (TextView) findPreviewView.findViewById(R.id.map_text);
            mapText.setText("No location was set");
        }


    }


}

