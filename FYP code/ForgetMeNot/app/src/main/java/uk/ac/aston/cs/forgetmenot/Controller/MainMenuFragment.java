package uk.ac.aston.cs.forgetmenot.Controller;

import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 02/04/2017.
 */

public class MainMenuFragment extends Fragment {
    private View view;
    private Button record;
    private Button find;
    private Button itemDetails;
    private Button trackItem;
    private TextView itemName;
    private ImageView itemImage;
    private DatabaseHelper dbHelper;
    private Item item;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper =new DatabaseHelper(getContext());
        fragmentManager =getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_menu_fragment ,container, false);
        record = (Button) view.findViewById(R.id.record_button);
        find = (Button) view.findViewById(R.id.find_button);
        itemDetails = (Button) view.findViewById(R.id.details_button);
        trackItem = (Button) view.findViewById(R.id.track_button);
        itemName = (TextView) view.findViewById(R.id.recent_item_name_view);
        itemImage = (ImageView) view.findViewById(R.id.recent_item_image_view);

        if(dbHelper.getItem().size() ==0) {
            disableButton(false, false, 150, 150);
            itemName.setText("No Items Recorded");
        }else{
            disableButton(true,true,250,250);
            item = dbHelper.getRecentItem();

            itemName.setText(item.getName());


            if (item.getImage() == null) {

                itemImage.setImageResource(item.setImageForMatchedItem(item.getName()));

            } else {
                Matrix rotatePicture = new Matrix();
                rotatePicture.postRotate(90);
                itemImage.setImageURI(Uri.parse(item.getImage()));

                itemImage.setImageMatrix(rotatePicture);
                itemImage.setImageURI(Uri.parse(item.getImage()));
                itemImage.setRotation(90);

            }



        }

        itemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               DisplayItemDetailsForFindFragment displayItemDetailsForFindFragment = new DisplayItemDetailsForFindFragment();

                fragmentTransaction.replace(R.id.main_menu_container, displayItemDetailsForFindFragment);
                Bundle bundleDetails = new Bundle();
                bundleDetails.putLong("Id", item.get_id());
                bundleDetails.putString("Name", item.getName());
                bundleDetails.putString("Note", item.getNote());
                bundleDetails.putString("ImagePath", item.getImage());
                bundleDetails.putDouble("Latitude", item.getLatitude());
                bundleDetails.putDouble("Longitude", item.getLongitude());
                bundleDetails.putBoolean("CreatedFromMenu", true);
                displayItemDetailsForFindFragment.setArguments(bundleDetails);


                fragmentTransaction.commit();
            }
        });

        trackItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackItemFragment trackItemFragment = new TrackItemFragment();

                fragmentTransaction.replace(R.id.main_menu_container, trackItemFragment);

                Bundle bundleDetails2 = new Bundle();
                bundleDetails2.putLong("Id",  item.get_id());
                bundleDetails2.putString("Name", item.getName());
                bundleDetails2.putDouble("Latitude", item.getLatitude());
                bundleDetails2.putDouble("Longitude",  item.getLongitude());
                bundleDetails2.putBoolean("createdFromMenu", true);
                trackItemFragment.setArguments(bundleDetails2);
                fragmentTransaction.commit();

            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordActivityIntent = new Intent(getActivity(), RecordActivity.class);
                startActivity(recordActivityIntent);
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findActivityIntent = new Intent(getActivity(), FindActivity.class);
                startActivity(findActivityIntent);
            }
        });
        return view;
    }

    public void disableButton(boolean track, boolean details, int trackInt, int detailsInt) {
        trackItem.setEnabled(track);
        itemDetails.setEnabled(details);

        trackItem.getBackground().setAlpha(trackInt);
        itemDetails.getBackground().setAlpha(detailsInt);

    }

    @Override
    public void onPause() {
        super.onPause();
        disableButton(true,true,250,250);
    }


}


