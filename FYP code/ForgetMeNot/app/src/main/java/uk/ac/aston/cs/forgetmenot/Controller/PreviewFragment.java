package uk.ac.aston.cs.forgetmenot.Controller;

import android.content.Context;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 01/02/2017.
 */

public class PreviewFragment extends Fragment implements OnMapReadyCallback {

    private View previewView;
    private Button doneButton;
    private boolean boolValueToBeSent = false;
    private boolean createdBool = false;
    PreviewFragmentInterface sendPreviewBoolean;
    private String name;
    private String note;
    private String imagePath;
    private TextView itemNameTextView;
    private TextView itemNotesTextView;
    private TextView mapText;
    private double latitude;
    private double longitude;
    private Item item;
    private ImageView previewImageView;
    Uri imageUri;
    private MapView mapView;
    GoogleMap myGoogleMap;

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

    /**
     * Makes the Record Activity whether the item can be saved or not
     */
    public interface PreviewFragmentInterface {

        public void setPreviewBoolean(boolean boolValue);

        public void getAttentionOfCreation(boolean createdBool);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = new Item();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        createdBool = true;
        sendPreviewBoolean.getAttentionOfCreation(createdBool);
        ((RecordActivity) getActivity()).disableButton(true, true, true, false, 250, 250, 250, 150);
        previewView = inflater.inflate(R.layout.preview_fragment, container, false);
        doneButton = (Button) previewView.findViewById(R.id.done_button);
        previewImageView = (ImageView) previewView.findViewById(R.id.image_view_2);

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
        itemNameTextView = (TextView) previewView.findViewById(R.id.preview_item_name);
        itemNotesTextView = (TextView) previewView.findViewById(R.id.preview_item_notes);

        itemNameTextView.setText(name);
        itemNotesTextView.setText(note);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolValueToBeSent = true;
                sendPreviewBoolean.setPreviewBoolean(boolValueToBeSent);

                ((RecordActivity) getActivity()).addToDatabase();


            }
        });

        Button cancel = (Button) previewView.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        ((RecordActivity) getActivity()).loadMainMenuActivity();
                    }
                });
        return previewView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendPreviewBoolean = (PreviewFragmentInterface) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    /**
     * Sets the values to their specific fields
     * @param name String name of the item
     * @param imagePath String uri of the image
     * @param note String note of the item
     * @param latitude double latitude of the item
     * @param longitude double longitude of the item
     */
    public void setAllTheValues(String name, String imagePath, String note, double latitude, double longitude) {
        this.name = name;
        this.note = note;
        this.imagePath = imagePath;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!(latitude == 0.0 && longitude == 0.0)) {
            mapView = (MapView) previewView.findViewById(R.id.preview_map_container);
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
        } else {
            mapText = (TextView) previewView.findViewById(R.id.map_text);
            mapText.setText("No location was set");
        }
    }


}
