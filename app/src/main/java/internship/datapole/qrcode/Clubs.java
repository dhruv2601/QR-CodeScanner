package internship.datapole.qrcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Clubs extends Fragment {

    private static final String TAG = "USerCard";
    private View view;
    private FloatingActionButton scanOwnCard;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AppCompatButton txt;

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
            name = "0";
            company = "0";
            CardObject1 obj = new CardObject1(Integer.parseInt(name), "No Card yet", "", company); // make a map of images and the service and provide that here
            results.add(obj);
        } else {
            String cardSpec = MainActivity.cards[Integer.parseInt(company)];
            for (int i = 0; i <= MainActivity.clubInd; i++) {
                name = clubsArr.get(i).first;
                company = clubsArr.get(i).second;
                CardObject1 obj = new CardObject1(Integer.parseInt(name), cardSpec, "", company); // make a map of images and the service and provide that here
                results.add(obj);
            }
        }
        Log.d(TAG, "name+company : " + name + "\n" + company);
        return results;
    }
}