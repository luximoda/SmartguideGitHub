package uk.ac.aston.cs.forgetmenot.Controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 25/01/2017.
 */
public class NoteFragment extends Fragment {

    private Button next;
    private EditText editText;
    private EditText itemName;
    private View noteView;
    private String noteString;
    NoteFragmentInterface sendNote = null;
    private String itemNameString;
    private Button cancel;
    private Button glassesButton;
    private Button keyButton;
    private Button walletButton;
    private Button penButton;
    private Button carButton;
    private Button bookButton;
    private String glassesString = "";
    private String keyString = "";
    private String walletString = "";
    private String penString = "";
    private String carString = "";
    private String bookString = "";
    private boolean isGlassesSelected = false;
    private boolean isBookSelected = false;
    private boolean isWalletSelected = false;
    private boolean isCarSelected = false;
    private boolean isPenSelected = false;
    private boolean isKeySelected = false;
    private StringBuilder sb;


    /**
     * Sends the note and item name to the Record Activity
     */
    public interface NoteFragmentInterface {
        public void setNote(String note, String itemName);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        noteView = inflater.inflate(R.layout.note_fragment, container, false);
        itemName = (EditText) noteView.findViewById(R.id.nameOfItem);
        next = (Button) noteView.findViewById(R.id.next_button);
        cancel = (Button) noteView.findViewById(R.id.cancel_button);
        editText = (EditText) noteView.findViewById(R.id.editText);
        glassesButton = (Button) noteView.findViewById(R.id.glassesButton);
        keyButton = (Button) noteView.findViewById(R.id.keyButton);
        walletButton = (Button) noteView.findViewById(R.id.walletButton);
        penButton = (Button) noteView.findViewById(R.id.penButton);
        carButton = (Button) noteView.findViewById(R.id.carButton);
        bookButton = (Button) noteView.findViewById(R.id.bookButton);
        sb = new StringBuilder();


        ((RecordActivity) getActivity()).disableButton(false, true, true, true, 150, 250, 250, 250);
        next.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        CameraFragment cameraFragment = new CameraFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, cameraFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        ((RecordActivity) getActivity()).loadMainMenuActivity();
                    }
                });

        itemName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                disableItemButton(true, true, true, true, true, true, 250, 250, 250, 250, 250, 250);
            }
        });


        glassesButton.setOnClickListener(
                new View.OnClickListener()

                {
                    public void onClick(View view) {

                        if (isGlassesSelected == false) {
                            isGlassesSelected = true;

                            multipleItemSelection("Glasses");
                            glassesButton.getBackground().setAlpha(150);
                        } else {
                            isGlassesSelected = false;

                            multipleItemSelection("Glasses");
                            glassesButton.getBackground().setAlpha(250);
                        }

                    }
                }

        );

        keyButton.setOnClickListener(
                new View.OnClickListener()

                {
                    public void onClick(View view) {

                        if (isKeySelected == false) {
                            isKeySelected = true;

                            multipleItemSelection("Key");
                            keyButton.getBackground().setAlpha(150);
                        } else {
                            isKeySelected = false;

                            multipleItemSelection("Key");
                            keyButton.getBackground().setAlpha(250);
                        }

                    }
                }

        );

        walletButton.setOnClickListener(
                new View.OnClickListener()

                {
                    public void onClick(View view) {

                        if (isWalletSelected == false) {
                            isWalletSelected = true;

                            multipleItemSelection("Wallet");
                            walletButton.getBackground().setAlpha(150);
                        } else {
                            isWalletSelected = false;

                            multipleItemSelection("Wallet");
                            walletButton.getBackground().setAlpha(250);
                        }

                    }
                }

        );
        penButton.setOnClickListener(
                new View.OnClickListener()

                {
                    public void onClick(View view) {


                        if (isPenSelected == false) {
                            isPenSelected = true;

                            multipleItemSelection("Pen");
                            penButton.getBackground().setAlpha(150);
                        } else {
                            isPenSelected = false;

                            multipleItemSelection("Pen");
                            penButton.getBackground().setAlpha(250);
                        }

                    }
                }

        );

        carButton.setOnClickListener(
                new View.OnClickListener()

                {
                    public void onClick(View view) {


                        if (isCarSelected == false) {
                            isCarSelected = true;

                            multipleItemSelection("Car");
                            carButton.getBackground().setAlpha(150);
                        } else {
                            isCarSelected = false;

                            multipleItemSelection("Car");
                            carButton.getBackground().setAlpha(250);
                        }

                    }
                }

        );

        bookButton.setOnClickListener(
                new View.OnClickListener()

                {
                    public void onClick(View view) {


                        if (isBookSelected == false) {
                            isBookSelected = true;

                            multipleItemSelection("Book");
                            bookButton.getBackground().setAlpha(150);
                        } else {
                            isBookSelected = false;

                            multipleItemSelection("Book");
                            bookButton.getBackground().setAlpha(250);
                        }

                    }
                }

        );


        return noteView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendNote = (NoteFragmentInterface) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }


    @Override
    public void onPause() {
        itemNameString = itemName.getText().toString();
        noteString = editText.getText().toString();
        sendNote.setNote(noteString, itemNameString);
        disableItemButton(true, true, true, true, true, true, 250, 250, 250, 250, 250, 250);

        super.onPause();
    }

    /**
     * Disables and changes the colour of the item button based on which item is selected
     * @param glasses boolean of whether glasses has been selected
     * @param key boolean of whether key has been selected
     * @param wallet boolean of whether wallet has been selected
     * @param pen boolean of whether pen has been selected
     * @param car boolean of whether car has been selected
     * @param book boolean of whether book has been selected
     * @param glassesInt int to change the alpha for glasses icon
     * @param keyInt int to change the alpha for key icon
     * @param walletInt int to change the alpha for wallet icon
     * @param penInt int to change the alpha for pen icon
     * @param carInt int to change the alpha for car icon
     * @param bookInt int to change the alpha for book icon
     */
    public void disableItemButton(boolean glasses, boolean key, boolean wallet,
                                  boolean pen, boolean car, boolean book,
                                  int glassesInt, int keyInt, int walletInt,
                                  int penInt, int carInt, int bookInt) {

        glassesButton.setEnabled(glasses);
        keyButton.setEnabled(key);
        walletButton.setEnabled(wallet);
        penButton.setEnabled(pen);
        carButton.setEnabled(car);
        bookButton.setEnabled(book);
        glassesButton.getBackground().setAlpha(glassesInt);
        keyButton.getBackground().setAlpha(keyInt);
        walletButton.getBackground().setAlpha(walletInt);
        penButton.getBackground().setAlpha(penInt);
        carButton.getBackground().setAlpha(carInt);
        bookButton.getBackground().setAlpha(bookInt);

    }

    /**
     * From the name of the item sent in, it changes the entire string being shown in text view displaying name of the item,
     * by either removing it or adding it with a comma depending on whether it already existed before
     * @param string String name of the item
     */
    public void multipleItemSelection(String string) {
        String[] strings;
        ArrayList<String> stringsArray = new ArrayList<>();
        boolean match = false;
        int position = 0;
        strings = sb.toString().split(",");

        for (int i = 0; i < strings.length; i++) {
            if (strings[0].equals("")) {
            } else {
                stringsArray.add(strings[i]);
            }
        }

        if (strings.length == 0) {
            sb.append(string);

        } else
            for (int i = 0; i < stringsArray.size(); i++) {
                if (stringsArray.get(i).contains(string)) {
                    match = true;
                    position = i;
                }
            }

        if (match == true) {
            stringsArray.remove(position);
        } else {
            stringsArray.add(string);
        }

        sb.setLength(0);
        for (int j = 0; j < stringsArray.size(); j++) {
            if (j == 0) {
                sb.append(stringsArray.get(j));
            } else {
                sb.append(",");
                sb.append(stringsArray.get(j));
            }
        }
        itemName.setText(sb.toString());
    }


}
