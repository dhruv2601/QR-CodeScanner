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
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class Hearts extends Fragment {

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

        List<Pair<String, String>> heartsArr = new ArrayList<>();
        heartsArr = MainActivity.heartsArr;

        if (MainActivity.heartInd == 0) {
            name = "No HEARTS added yet";
            company = "1";
            CardObject1 obj = new CardObject1(0, name, "", company); // make a map of images and the service and provide that here
            results.add(0, obj);
        } else {
            for (int i = 0; i < MainActivity.heartInd; i++) {
                name = heartsArr.get(i).first;
                company = heartsArr.get(i).second;
                Log.d(TAG, "name: " + name + "\n" + "company: " + company);
                String cardSpec = MainActivity.cards[Integer.parseInt(company)];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                CardObject1 obj = new CardObject1(0, cardSpec + " of Hearts", format, company); // make a map of images and the service and provide that here
                results.add(obj);
            }
        }
        fabCount.setImageBitmap(textAsBitmap(MainActivity.heartInd.toString(), 40, Color.WHITE));

        Log.d(TAG, "name+company : " + name + "\n" + company);
        return results;
    }
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