package internship.datapole.qrcode;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class Spades extends Fragment {

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
            mRecyclerView.setLayoutManager(mLayoutManager);
            fabCount = (FloatingActionButton) view.findViewById(R.id.fab_count);
            mAdapter = new allCardRecyclerViewAdapter(getDataSet(), view.getContext());
            mRecyclerView.setAdapter(mAdapter);

//            mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//                @Override
//                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//
//
//                    final CharSequence[] items = {" Clubs ", " Hearts ", " Diamond "};
//                    // arraylist to keep the selected items
//                    final ArrayList<Integer> seletedItems = new ArrayList();
//
////                    pos = getAdapterPosition();
//                    final int toRemove = allCardRecyclerViewAdapter.pos;
//                    Log.d(TAG, "pos:::: " + allCardRecyclerViewAdapter.pos);
//                    AlertDialog dialog = new AlertDialog.Builder(view.getContext())
//                            .setTitle("Select a category to shift to")
//                            .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
//                                    if (isChecked) {
//
//                                        // If the user checked the item, add it to the selected items
//                                        seletedItems.add(indexSelected);
//                                    } else if (seletedItems.contains(indexSelected)) {
//                                        // Else, if the item is already in the array, remove it
//                                        seletedItems.remove(Integer.valueOf(indexSelected));
//                                    }
//                                }
//                            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                    Pair<String, String> removed = MainActivity.spadesArr.get(toRemove);
//                                    MainActivity.spadeInd--;
//                                    Spades.delFromSpades(toRemove);
//
//                                    int moveTO = (seletedItems.get(0));
//                                    if (moveTO == 0) {
//                                        MainActivity.clubsArr.add(removed);
//                                        MainActivity.clubInd++;
//                                    } else if (moveTO == 1) {
//                                        MainActivity.diamondsArr.add(removed);
//                                        MainActivity.diaInd++;
//                                    } else if (moveTO == 2) {
//                                        MainActivity.heartsArr.add(removed);
//                                        MainActivity.heartInd++;
//                                    }
//                                    Spades.refresh();
//                                    //  Your code when user clicked on OK
//                                    //  You can write the code  to save the selected item here
//                                }
//                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//                                    //  Your code when user clicked on Cancel
//                                }
//                            }).create();
//                    dialog.show();
//
//                    return false;
//                }
//
//                @Override
//                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//                }
//
//                @Override
//                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//                }
//            });


//            mRecyclerView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//
//                    final CharSequence[] items = {" Clubs ", " Hearts ", " Diamond "};
//                    // arraylist to keep the selected items
//                    final ArrayList<Integer> seletedItems = new ArrayList();
//
//                    final int toRemove = mAdapter.pos;
//
//                    AlertDialog dialog = new AlertDialog.Builder(getContext())
//                            .setTitle("Select a category to shift to")
//                            .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
//                                    if (isChecked) {
//                                        // If the user checked the item, add it to the selected items
//                                        seletedItems.add(indexSelected);
//                                    } else if (seletedItems.contains(indexSelected)) {
//                                        // Else, if the item is already in the array, remove it
//                                        seletedItems.remove(Integer.valueOf(indexSelected));
//                                    }
//                                }
//                            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                    Pair<String, String> removed = MainActivity.spadesArr.get(toRemove);
//                                    delFromSpades(toRemove);
//
//                                    int moveTO = (seletedItems.get(0));
//                                    if (moveTO == 0) {
//                                        MainActivity.clubsArr.add(removed);
//                                        MainActivity.clubInd++;
//                                    } else if (moveTO == 1) {
//                                        MainActivity.diamondsArr.add(removed);
//                                        MainActivity.diaInd++;
//                                    } else if (moveTO == 2) {
//                                        MainActivity.heartsArr.add(removed);
//                                        MainActivity.heartInd++;
//                                    }
//
//                                    //  Your code when user clicked on OK
//                                    //  You can write the code  to save the selected item here
//                                }
//                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int id) {
//                                    //  Your code when user clicked on Cancel
//                                }
//                            }).create();
//                    dialog.show();
//
//                    return false;
//                }
//            });


            fabDel = (FloatingActionButton) view.findViewById(R.id.fab_del);
            fabDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    String plays[]
                            = new String[MainActivity.spadesArr.size()];
                    for (int i = 0; i < MainActivity.spadesArr.size(); i++) {
                        plays[i] = String.valueOf(Integer.parseInt(MainActivity.spadesArr.get(i).second) + 1);
                    }
                    for (int i = 0; i < MainActivity.spadesArr.size(); i++) {
                        Log.d(TAG, "Pla: " + plays[i]);
                    }
                    for (int i = 0; i < MainActivity.spadesArr.size(); i++) {
                        map.put(i, MainActivity.spadesArr.get(i).second);
                    }
                    final boolean[] checkedColors = new boolean[MainActivity.spadesArr.size()];
                    final List<String> colorsList = Arrays.asList(plays);
                    for (int i = 0; i < MainActivity.spadesArr.size(); i++) {
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
                            for (int i = 0; i < MainActivity.spadesArr.size(); i++) {
                                int flag = 0;
                                for (int j = temp; j < delInd; j++) {
                                    if (delMap.get(j) == i) {
                                        temp++;
                                        flag = 1;
                                        break;
                                    }
                                }
                                if (flag == 0) {
                                    tempArr.add(MainActivity.spadesArr.get(i));
                                }
//                                    MainActivity.diaInd--;
//                                    MainActivity.diamondsArr.remove(delMap.get(i));
                            }
                            MainActivity.spadesArr = tempArr;
                            MainActivity.spadeInd -= delInd;
                            for (int i = 0; i < MainActivity.spadesArr.size(); i++) {
                                Log.d(TAG, "left:: " + MainActivity.spadesArr.get(i).second);
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

    public static void delFromSpades(int x) {
        List<Pair<String, String>> temp = new ArrayList<Pair<String, String>>();
        for (int i = 0; i < MainActivity.spadesArr.size(); i++) {
            if (i != x) {
                temp.add(MainActivity.spadesArr.get(i));
            }
        }
        MainActivity.spadesArr = temp;
    }


    public static void refresh() {
        fabCount.setImageBitmap(textAsBitmap(MainActivity.spadeInd.toString(), 40, Color.WHITE));
        String name = "";
        String company = "";
        ArrayList results = new ArrayList<CardObject1>();

        List<Pair<String, String>> spadesArr = new ArrayList<>();
        spadesArr = MainActivity.spadesArr;

        if (MainActivity.spadeInd == 0) {
            name = "No SPADES added yet";
            company = "4";
            CardObject1 obj = new CardObject1(0, name, "", company); // make a map of images and the service and provide that here
            results.add(obj);
        } else {
            for (int i = 0; i < MainActivity.spadeInd; i++) {
                name = spadesArr.get(i).first;
                company = spadesArr.get(i).second;
                Log.d(TAG, "name: " + name + "\n" + "company: " + company);
                String cardSpec = MainActivity.cards[Integer.parseInt(company)];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());

                CardObject1 obj = new CardObject1(0, cardSpec + " of " + MainActivity.spadesArr.get(i).first, format, company); // make a map of images and the service and provide that here
                results.add(obj);
            }
        }
        mAdapter.swap(results);
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
            results.add(obj);
        } else {
            for (int i = 0; i < MainActivity.spadeInd; i++) {
                name = spadesArr.get(i).first;
                company = spadesArr.get(i).second;
                Log.d(TAG, "name: " + name + "\n" + "company: " + company);
                String cardSpec = MainActivity.cards[Integer.parseInt(company)];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());

                CardObject1 obj = new CardObject1(0, cardSpec + " of " + MainActivity.spadesArr.get(i).first, format, company); // make a map of images and the service and provide that here
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