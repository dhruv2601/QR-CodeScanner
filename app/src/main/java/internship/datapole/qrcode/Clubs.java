package internship.datapole.qrcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class Clubs extends Fragment {

    private static final String TAG = "UserCard";
    private View view;
    private FloatingActionButton scanOwnCard;
    private RecyclerView mRecyclerView;
    public static allCardRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static FloatingActionButton fabCount;
    private FloatingActionButton fabDel;
    HashMap<Integer, String> map = new HashMap<>();
    int selected = 0;
    int delInd = 0;
    HashMap<Integer, Integer> delMap = new HashMap<>();

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
            fabCount = (FloatingActionButton) view.findViewById(R.id.fab_count);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new allCardRecyclerViewAdapter(getDataSet(), view.getContext());
            mRecyclerView.setAdapter(mAdapter);
            fabDel = (FloatingActionButton) view.findViewById(R.id.fab_del);
            fabDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    String plays[]
                            = new String[MainActivity.clubsArr.size()];
                    for (int i = 0; i < MainActivity.clubsArr.size(); i++) {
                        plays[i] = String.valueOf(Integer.parseInt(MainActivity.clubsArr.get(i).second) + 1);
                    }
                    for (int i = 0; i < MainActivity.clubsArr.size(); i++) {
                        Log.d(TAG, "Pla: " + plays[i]);
                    }
                    for (int i = 0; i < MainActivity.clubsArr.size(); i++) {
                        map.put(i, MainActivity.clubsArr.get(i).second);
                    }
                    final boolean[] checkedColors = new boolean[MainActivity.clubsArr.size()];
                    final List<String> colorsList = Arrays.asList(plays);
                    for (int i = 0; i < MainActivity.clubsArr.size(); i++) {
                        checkedColors[i] = false;
                    }
                    builder.setMultiChoiceItems(plays, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

//                             Update the current focused item's checked status
                            checkedColors[which] = isChecked;
                            Log.d(TAG, "which::: " + which);
                            // Get the current focused item
                            String currentItem = colorsList.get(which);
                            delMap.put(delInd++, which);

                            Log.d(TAG, "selected: " + currentItem);
                        }
                    });

                    // Specify the dialog is not cancelable
                    builder.setCancelable(false);

                    // Set a title for alert dialog
                    builder.setTitle("Select cards to delete");

                    final List<Pair<String, String>> tempArr = new ArrayList<Pair<String, String>>();

                    // Set the positive/yes button click listener
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int temp = 0;
                            for (int i = 0; i < MainActivity.clubsArr.size(); i++) {
                                int flag = 0;
                                for (int j = temp; j < delInd; j++) {
                                    if (delMap.get(j) == i) {
                                        temp++;
                                        flag = 1;
                                        break;
                                    }
                                }
                                if (flag == 0) {
                                    tempArr.add(MainActivity.clubsArr.get(i));
                                }
//                                    MainActivity.diaInd--;
//                                    MainActivity.diamondsArr.remove(delMap.get(i));
                            }
                            MainActivity.clubsArr = tempArr;
                            MainActivity.clubInd -= delInd;
                            for (int i = 0; i < MainActivity.clubsArr.size(); i++) {
                                Log.d(TAG, "left:: " + MainActivity.clubsArr.get(i).second);
                            }
                            refresh();
                        }
                    });

                    // Set the neutral/cancel button click listener
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when click the neutral button
                        }
                    });

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
                }
            });

        }
        return view;
    }


    public static void refresh() {
        fabCount.setImageBitmap(textAsBitmap(MainActivity.clubInd.toString(), 40, Color.WHITE));
        String name = "";
        String company = "";
        ArrayList results = new ArrayList<CardObject1>();

        List<Pair<String, String>> clubsArr = new ArrayList<>();
        clubsArr = MainActivity.clubsArr;

        if (MainActivity.clubInd == 0) {
            name = "No CLUBS added yet";
            company = "1";
            CardObject1 obj = new CardObject1(0, name, "", company); // make a map of images and the service and provide that here
            results.add(obj);
        } else {
            Log.d(TAG, "ClubIND:: " + MainActivity.clubInd);
            for (int i = 0; i < MainActivity.clubInd; i++) {
                name = clubsArr.get(i).first;
                company = clubsArr.get(i).second;
                Log.d(TAG, "name: " + name + "\n" + "company: " + company);
                String cardSpec = MainActivity.cards[Integer.parseInt(company)];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                CardObject1 obj = new CardObject1(0, cardSpec + " of " + MainActivity.clubsArr.get(i).first, format, company); // make a map of images and the service and provide that here
                results.add(obj);
            }
        }
        mAdapter.swap(results);
    }

    private ArrayList<CardObject1> getDataSetEmpty() {
        return null;
    }

    public ArrayList<CardObject1> getDataSet() {
        String name = "";
        String company = "";
        ArrayList results = new ArrayList<CardObject1>();

        List<Pair<String, String>> clubsArr = new ArrayList<>();
        clubsArr = MainActivity.clubsArr;

        if (MainActivity.clubInd == 0) {
            name = "No CLUBS added yet";
            company = "1";
            CardObject1 obj = new CardObject1(0, name, "", company); // make a map of images and the service and provide that here
            results.add(obj);
        } else {
            Log.d(TAG, "ClubIND:: " + MainActivity.clubInd);
            for (int i = 0; i < MainActivity.clubInd; i++) {
                name = clubsArr.get(i).first;
                company = clubsArr.get(i).second;
                Log.d(TAG, "name: " + name + "\n" + "company: " + company);
                String cardSpec = MainActivity.cards[Integer.parseInt(company)];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                CardObject1 obj = new CardObject1(0, cardSpec + " of " + MainActivity.clubsArr.get(i).first, format, company); // make a map of images and the service and provide that here
                results.add(obj);
            }
        }

        fabCount.setImageBitmap(textAsBitmap(MainActivity.clubInd.toString(), 40, Color.WHITE));

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