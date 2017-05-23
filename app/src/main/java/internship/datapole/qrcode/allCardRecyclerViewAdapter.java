package internship.datapole.qrcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhruv on 29/12/16.
 */

public class allCardRecyclerViewAdapter
        extends RecyclerView
        .Adapter<allCardRecyclerViewAdapter
        .DataObjectHolder> {

    public static final String TAG = "myRecViewAdapter";
    private static ArrayList<CardObject1> mCardSet;
    private static MyClickListener myClickListener;
    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView imageDrawable;
        ImageView imgDel;
        TextView txtName;
        TextView txtPosition;
        TextView txtCompany;


        public DataObjectHolder(View itemView) {
            super(itemView);
            imageDrawable = (ImageView) itemView.findViewById(R.id.img_card);
            imgDel = (ImageView) itemView.findViewById(R.id.del);
            txtName = (TextView) itemView.findViewById(R.id.txt_name_all_card);
            txtPosition = (TextView) itemView.findViewById(R.id.txt_position_all_card);
            txtCompany = (TextView) itemView.findViewById(R.id.txt_company_all_card);

            final int tabPos = MainActivity.tabLayout.getSelectedTabPosition();
            Log.d(TAG, "tabPos: " + tabPos);

            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "getAda: " + getAdapterPosition());
                    int x = getAdapterPosition();

                    Log.d(TAG, "getCar: " + mCardSet.size());
                    mCardSet.remove(x);
                    Log.d(TAG, "after:: ");
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeRemoved(0, MainActivity.heartInd);
                    notifyItemRangeInserted(0, MainActivity.heartInd - 1);
                    notifyDataSetChanged();

//                    notifyItemRangeChanged(getAdapterPosition(), mCardSet.size());

                    if (MainActivity.tabPos == 0) {
                        if (MainActivity.clubInd > 0) {
                            MainActivity.clubsArr.remove(x);
                            MainActivity.clubInd--;
                        }
                    } else if (MainActivity.tabPos == 1) {
                        if (MainActivity.diaInd > 0) {
                            MainActivity.diamondsArr.remove(x);
                            MainActivity.diaInd--;
                        }
                    } else if (MainActivity.tabPos == 2) {
                        if (MainActivity.heartInd > 0) {
                            Log.d(TAG, "hearInd: " + MainActivity.heartInd);
                            List<Pair<String, String>> temp = new ArrayList<Pair<String, String>>();
                            int ind = 0;
                            for (int i = 0; i < MainActivity.heartsArr.size(); i++) {
                                if (i != x) {
                                    temp.add(ind++, MainActivity.heartsArr.get(i));
                                }
                            }
                            MainActivity.heartsArr = temp;
//                            MainActivity.heartsArr.remove(x);
                            MainActivity.heartInd--;
                        }
                    } else if (MainActivity.tabPos == 3) {
                        if (MainActivity.spadeInd > 0) {
                            MainActivity.spadesArr.remove(x);
                            MainActivity.spadeInd--;
                        }
                    }
                }
            });

            Log.d(TAG, "dataObjHolderALLCARDS");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(v.getContext(), "", Toast.LENGTH_SHORT).show();
//            myClickListener.onItemClick(getAdapterPosition(), v);
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

    public void swap(ArrayList<CardObject1> myCardSetRec) {
        mCardSet.clear();
        mCardSet.addAll(myCardSetRec);
        notifyDataSetChanged();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_cards_list_cardview, parent, false);

        int id = ((ViewGroup) parent.getParent()).getId();
        Log.d(TAG, "ID:::: " + id);

        Log.d(TAG, "onCrateViewHolderOfAllCArds");
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.txtName.setText(mCardSet.get(position).getTxtName());
        Log.d(TAG, "mCardValS: " + mCardSet.get(position).getTxtName());
        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(String.valueOf(position + 1), Color.TRANSPARENT, 30);

        int id = holder.imgDel.getId();
        Log.d(TAG, "idllll " + id);
        holder.imageDrawable.setImageDrawable(drawable1);
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

    public void removeAt(int position) {
        mCardSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCardSet.size());
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}