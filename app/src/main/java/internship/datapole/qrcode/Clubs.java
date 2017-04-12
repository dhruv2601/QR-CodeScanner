package internship.datapole.qrcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
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

public class Clubs extends Fragment {

    private static final String TAG = "UserCard";
    private View view;
    private FloatingActionButton scanOwnCard;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        List<Pair<String, String>> clubsArr = new ArrayList<>();
        clubsArr = MainActivity.clubsArr;

        if (MainActivity.clubInd == 0) {
            name = "No CLUBS added yet";
            company = "1";
            CardObject1 obj = new CardObject1(0, name, "", company); // make a map of images and the service and provide that here
            results.add(0, obj);
        } else {
            for (int i = 0; i < MainActivity.clubInd; i++) {
                name = clubsArr.get(i).first;
                company = clubsArr.get(i).second;
                Log.d(TAG, "name: " + name + "\n" + "company: " + company);
                String cardSpec = MainActivity.cards[Integer.parseInt(company)];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                CardObject1 obj = new CardObject1(0, cardSpec + " of clubs", format, company); // make a map of images and the service and provide that here
                results.add(obj);
            }
        }
        Log.d(TAG, "name+company : " + name + "\n" + company);
        return results;
    }
}