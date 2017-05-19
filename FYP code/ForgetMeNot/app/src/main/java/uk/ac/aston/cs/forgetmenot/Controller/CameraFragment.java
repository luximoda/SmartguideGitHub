package uk.ac.aston.cs.forgetmenot.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import uk.ac.aston.cs.forgetmenot.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Dhanishrao_2 on 25/01/2017.
 */

public class CameraFragment extends Fragment implements View.OnClickListener {


    ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    CameraFragmentInterface sendPicture;
    String imagePath = null;
    private View myView;

    /**
     * Sends the uri of the camera to the record activity via the interface
     */
    public interface CameraFragmentInterface {
        public void setCamera(String camera);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.camera_fragment, container, false);

        ((RecordActivity) getActivity()).disableButton(true, false, true, true, 250, 150, 250, 250);
        Button takePicture = (Button) myView.findViewById(R.id.take_picture_button);


        takePicture.setOnClickListener(this);

        Button next = (Button) myView.findViewById(R.id.next_button_2);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                MapFragment mapFragment = new MapFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, mapFragment, "CAMERA_FRAGMENT")
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
    public void onClick(View v) {

        dispatchTakePictureIntent();
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView = (ImageView) myView.findViewById(R.id.image_view);
            imageView.setImageBitmap(imageBitmap);
            Uri imageUri = data.getData();
            imagePath = imageUri.toString();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            sendPicture = (CameraFragmentInterface) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        sendPicture.setCamera(imagePath);
    }
}
