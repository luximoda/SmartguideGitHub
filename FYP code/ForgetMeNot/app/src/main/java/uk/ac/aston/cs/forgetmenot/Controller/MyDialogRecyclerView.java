package uk.ac.aston.cs.forgetmenot.Controller;

import android.content.Context;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 03/04/2017.
 */

public class MyDialogRecyclerView extends RecyclerView.Adapter<MyDialogRecyclerView.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Item> items = Collections.emptyList();
    private DatabaseHelper dbHelper;
    private FragmentManager fragmentManager;
    private Item item;
    private Context fragmentContext;

    /**
     * Creates the dialog recycler view based on the context, list of items and the recycler view fragment
     * @param context Context of where it is used
     * @param items List of items
     * @param fragmentActivity the DialogAiRecyclerViewFragment fragment
     */
    public MyDialogRecyclerView(Context context, List<Item> items, DialogAiRecyclerViewFragment fragmentActivity) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        fragmentManager = fragmentActivity.getActivity().getSupportFragmentManager();
        fragmentContext = fragmentActivity.getContext();
    }

    @Override
    public MyDialogRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.preview_item_dialog, parent, false);
        dbHelper = new DatabaseHelper(parent.getContext());

        MyDialogRecyclerView.MyViewHolder holder = new MyDialogRecyclerView.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyDialogRecyclerView.MyViewHolder holder, int position) {
        item = items.get(position);
        final int countUpdate = item.getCount() + 1;
        holder.text.setText(item.getName());
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.updateArchiveItem(null, "Undefined notes", countUpdate, item.get_id());
                dbHelper.deleteItem(dbHelper.getItemByName(item.getName()).get_id());
                dbHelper.addItem(item.getName(), item.getImage(), item.getNote(), item.getLatitude(), item.getLongitude(), item.getCount(), item.getBool(), dbHelper.getDateTime());
                items.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());
                notifyItemRangeChanged(holder.getLayoutPosition(), items.size());
                Toast.makeText(fragmentContext, "Item added", Toast.LENGTH_LONG).show();

            }
        });



        if (item.getImage() == null) {

            holder.image.setImageResource(item.setImageForMatchedItem(item.getName()));

        } else {
            Matrix rotatePicture = new Matrix();
            rotatePicture.postRotate(90);
            holder.image.setImageURI(Uri.parse(item.getImage()));

            holder.image.setImageMatrix(rotatePicture);
            holder.image.setImageURI(Uri.parse(item.getImage()));
            holder.image.setRotation(90);

        }


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    /**
     * Class for creating and displaying the details of only one item
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView image;
        Button save;

        /**
         * Constructor for holding details of only one item and displaying it
         * @param itemView the view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.itemName);
            image = (ImageView) itemView.findViewById(R.id.image_view_2);
            save = (Button) itemView.findViewById(R.id.save_button);

        }
    }


}

