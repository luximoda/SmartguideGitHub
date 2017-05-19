package uk.ac.aston.cs.forgetmenot.Controller;

import android.content.Context;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 12/02/2017.
 */

public class MyRecyclerView extends RecyclerView.Adapter<MyRecyclerView.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Item> items = Collections.emptyList();
    private DatabaseHelper dbHelper;
    private FragmentTransaction fragmentTransaction;
    private DisplayItemDetailsForFindFragment displayItemDetailsForFindFragment;
    private FragmentManager fragmentManager;

    private Item item;

    /**
     * Creates the recycler view
     * @param context context of where it is used
     * @param items List of type Item
     * @param fragmentActivity Fragment RecyclerViewFragment
     */
    public MyRecyclerView(Context context, List<Item> items, RecyclerViewFragment fragmentActivity) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        fragmentManager = fragmentActivity.getActivity().getSupportFragmentManager();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.one_item_grid_view, parent, false);
        dbHelper = new DatabaseHelper(parent.getContext());
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        item = items.get(position);
        holder.text.setText(item.getName());

        holder.date.setText((item.getDate()));

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
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item currentItem = items.get(holder.getLayoutPosition());
                fragmentTransaction = fragmentManager.beginTransaction();
                displayItemDetailsForFindFragment = new DisplayItemDetailsForFindFragment();

                fragmentTransaction.replace(R.id.find_frame_container, displayItemDetailsForFindFragment);
                Bundle bundleDetails = new Bundle();
                bundleDetails.putLong("Id", currentItem.get_id());
                bundleDetails.putString("Name", currentItem.getName());
                bundleDetails.putString("Note", currentItem.getNote());
                bundleDetails.putString("ImagePath", currentItem.getImage());
                bundleDetails.putDouble("Latitude", currentItem.getLatitude());
                bundleDetails.putDouble("Longitude", currentItem.getLongitude());
                bundleDetails.putBoolean("CreatedFromMenu", false);
                displayItemDetailsForFindFragment.setArguments(bundleDetails);


                fragmentTransaction.commit();

            }
        });
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View dialogView = inflater.inflate(R.layout.delete_grid_item_dialog, null);
                Button cancel = (Button) dialogView.findViewById(R.id.cancel_delete_button);
                Button done = (Button) dialogView.findViewById(R.id.enable_delete_button);





                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Item currentItem = items.get(holder.getLayoutPosition());

                        dbHelper.deleteItem(currentItem.get_id());

                        items.remove(holder.getLayoutPosition());

                        notifyItemRemoved(holder.getLayoutPosition());

                        notifyItemRangeChanged(holder.getLayoutPosition(), items.size());
                      //  Toast.makeText(v.getContext(), "item no" + holder.getLayoutPosition() + "item name" + currentItem.getName(), Toast.LENGTH_LONG).show();
                        dialog.cancel();

                    }
                });


                return false;
            }
        });

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
        TextView date;

        /**
         * Constructor for holding details of only one item and displaying it
         * @param itemView the view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.item_name_view);
            image = (ImageView) itemView.findViewById(R.id.item_image_view);
            date = (TextView) itemView.findViewById(R.id.item_date_view);
        }
    }


}
