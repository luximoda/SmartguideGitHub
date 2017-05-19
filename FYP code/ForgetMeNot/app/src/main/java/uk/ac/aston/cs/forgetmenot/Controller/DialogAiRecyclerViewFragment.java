package uk.ac.aston.cs.forgetmenot.Controller;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 03/04/2017.
 */

public class DialogAiRecyclerViewFragment extends DialogFragment {
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    MyDialogRecyclerView adapter;
    MainMenuActivity mainMenuActivity;
    List<Item> itemlist;
    Button back;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        dbHelper = new DatabaseHelper(getContext());
        View layout = inflater.inflate(R.layout.ai_list_view_fragment, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        back = (Button) layout.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MainMenuFragment mainMenuFragment = new MainMenuFragment();
                fragmentTransaction.replace(R.id.main_menu_container, mainMenuFragment);
                fragmentTransaction.commit();
            }
        });
        adapter = new MyDialogRecyclerView(getActivity(),itemlist,this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;

    }

    /**
     * Gets the item from the main menu activity and return it
     * @return a List of type item
     */
    public List<Item> getItemDetails(){
        return mainMenuActivity.getList();

    }

    /**
     * Sets itemlist to the list of item passed in
     * @param itemlist the List of type Item
     */
    public void setList(List<Item> itemlist){
        this.itemlist = itemlist;
    }

}
