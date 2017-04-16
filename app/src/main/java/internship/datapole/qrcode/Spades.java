package internship.datapole.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class Spades extends Fragment {

    private static final String TAG = "UserCard";
    private View view;
    private FloatingActionButton scanOwnCard;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fabCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.clubs_frag, container, false);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.own_card_rv);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            fabCount = (FloatingActionButton) view.findViewById(R.id.fab_count);
            mAdapter = new allCardRecyclerViewAdapter(getDataSet(), view.getContext());
            mRecyclerView.setAdapter(mAdapter);

        }
        return view;
    }

    private ArrayList<CardObject1> getDataSetEmpty() {
        return null;
    }

    private ArrayList<CardObject1> getDataSet() {
        String name = "";
        String company = "";
        ArrayList results = new ArrayList<CardObject1>();

        List<Pair<String, String>> spadesArr = new ArrayList<>();
        spadesArr = MainActivity.spadesArr;

        if (MainActivity.spadeInd == 0) {
            name = "No SPADES added yet";
            company = "4";
            CardObject1 obj = new CardObject1(0, name, "", company); // make a map of images and the service and provide that here
            results.add(0, obj);
        } else {
            for (int i = 0; i < MainActivity.spadeInd; i++) {
                name = spadesArr.get(i).first;
                company = spadesArr.get(i).second;
                Log.d(TAG, "name: " + name + "\n" + "company: " + company);
                String cardSpec = MainActivity.cards[Integer.parseInt(company)];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());

                CardObject1 obj = new CardObject1(0, cardSpec + " of spades", format, company); // make a map of images and the service and provide that here
                results.add(obj);
            }
        }
        fabCount.setImageBitmap(textAsBitmap(MainActivity.spadeInd.toString(), 40, Color.WHITE));
        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(mRecyclerView);

        Log.d(TAG, "name+company : " + name + "\n" + company);
        return results;
    }

    ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
        //and in your imlpementaion of
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // get the viewHolder's and target's positions in your adapter data, swap them
            Collections.swap(getDataSet(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
            // and notify the adapter that its dataset has changed
            mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //TODO
        }

        //defines the enabled move directions in each state (idle, swiping, dragging).
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
        }
    };

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}