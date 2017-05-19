package uk.ac.aston.cs.forgetmenot.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 12/02/2017.
 */

public class RecyclerViewFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    MyRecyclerView adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbHelper = new DatabaseHelper(getContext());
        View layout = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        adapter = new MyRecyclerView(getActivity(), getItemDetails(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return layout;

    }

    /**
     * Returns the list of items from the database
     * @return list of type Item
     */
    public List<Item> getItemDetails() {
        List<Item> items = dbHelper.getItem();
        return items;
    }


}
