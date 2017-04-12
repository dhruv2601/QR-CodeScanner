package internship.datapole.qrcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;

/**
 * Created by dhruv on 29/12/16.
 */

public class allCardRecyclerViewAdapter
        extends RecyclerView
        .Adapter<allCardRecyclerViewAdapter
        .DataObjectHolder> {

    public static final String TAG = "myRecViewAdapter";
    private ArrayList<CardObject1> mCardSet;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView imageDrawable;
        TextView txtName;
        TextView txtPosition;
        TextView txtCompany;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageDrawable = (ImageView) itemView.findViewById(R.id.img_card);
            txtName = (TextView) itemView.findViewById(R.id.txt_name_all_card);
            txtPosition = (TextView) itemView.findViewById(R.id.txt_position_all_card);
            txtCompany = (TextView) itemView.findViewById(R.id.txt_company_all_card);

            Log.d(TAG, "dataObjHolderALLCARDS");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Intent i = new Intent(v.getContext(), ShowCardDetails.class);
//            int pos = getAdapterPosition();
//            i.putExtra("CardPosition", pos);
//
////            i.putExtra("")                                    // yahan pe sending timke par snd the phone no.s and ither details
//            v.getContext().startActivity(i);
            Toast.makeText(v.getContext(), "", Toast.LENGTH_SHORT).show();
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public allCardRecyclerViewAdapter(ArrayList<CardObject1> myCardSet, Context context) {
        mCardSet = myCardSet;
        for (int i = 0; i < myCardSet.size(); i++) {
            Log.d(TAG, "mCardSetVal: " + mCardSet.get(i).getTxtName());
        }
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_cards_list_cardview, parent, false);

        Log.d(TAG, "onCrateViewHolderOfAllCArds");
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.txtName.setText(mCardSet.get(position).getTxtName());
        Log.d(TAG, "mCardValS: " + mCardSet.get(position).getTxtName());
        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(String.valueOf(position+1), Color.BLACK, 30);

        holder.imageDrawable.setImageDrawable(drawable1);
//        if (mCardSet.get(position).getmDrawableImage() ==1 ) {      // red
//            holder.imageDrawable.setImageResource(R.drawable.red);
//        } else {
//            holder.imageDrawable.setImageResource(R.drawable.black);    //black = 0
//        }
        holder.txtPosition.setText(mCardSet.get(position).getTxtPosition());
        holder.txtCompany.setText("");
    }

    public void addItem(CardObject1 cardObject, int index) {
        mCardSet.add(index, cardObject);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mCardSet.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        int x = 0;
        if (mCardSet == null) {
            x = 0;
        } else {
            x = mCardSet.size();
        }
        return x;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}