package internship.datapole.qrcode;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.github.skyhacker2.sqliteonweb.SQLiteOnWeb;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    ImageView imgScanQR;
    ImageView imgGrad;
    TextView txtResult;
    private IntentIntegrator integrator;
    public static TabLayout tabLayout;
    public ViewPager viewPager;
    public static List<Pair<String, String>> clubsArr;
    public static List<Pair<String, String>> diamondsArr;
    public static List<Pair<String, String>> heartsArr;
    public static List<Pair<String, String>> spadesArr;

    public static String[] cards = new String[]{"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "Jack", "Queen", "King"};

    public static int tabPos = 0;
//    public static Integer clubsArr[];
//    public static Integer diamondsArr[];
//    public static Integer heartsArr[];
//    public static Integer spadesArr[];

    public static Integer clubInd = 0;
    public static Integer diaInd = 0;
    public static Integer heartInd = 0;
    public static Integer spadeInd = 0;

    public static String cardType;
    public PageAdapter adapter;
    //    private CompoundBarcodeView barcodeView;
    private DecoratedBarcodeView barcodeView;

    public DataBaseHandler db;

    private CaptureManager capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

//        db = new DataBaseHandler(MainActivity.this);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.bar_code);
        barcodeView.decodeContinuous(callback);
        capture = new CaptureManager(this, barcodeView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        clubsArr = new ArrayList<>();
        diamondsArr = new ArrayList<>();
        heartsArr = new ArrayList<>();
        spadesArr = new ArrayList<>();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addTab(tabLayout.newTab().setText("Clubs"));    //0
        tabLayout.addTab(tabLayout.newTab().setText("Diamonds"));      //1
        tabLayout.addTab(tabLayout.newTab().setText("Hearts"));      //2
        tabLayout.addTab(tabLayout.newTab().setText("Spades"));      //3

        Log.d(TAG, "initAdapter");
        viewPager = (ViewPager) findViewById(R.id.pager);
        Log.d(TAG, "initAdapter1");

        Log.d(TAG, "getSupport: " + this.getSupportFragmentManager() + "\n" + tabLayout.getTabCount());
        adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Clubs");
        tabLayout.getTabAt(1).setText("Diamonds");
        tabLayout.getTabAt(2).setText("Hearts");
        tabLayout.getTabAt(3).setText("Spades");
//        integrator = new IntentIntegrator(this);

        imgGrad = (ImageView) findViewById(R.id.img_grad);
        final int[] imageArray = {R.drawable.atlas_gradient, R.drawable.blue_gradient};

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                imgGrad.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 2300);
                Animation myFadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadein);
                imgGrad.startAnimation(myFadeInAnimation);
            }
        };

        handler.postDelayed(runnable, 2300);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tabPos = position;
                Log.d(TAG, "pos:: " + position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        imgScanQR = (ImageView) findViewById(R.id.open_cam);
//        txtResult = (TextView) findViewById(R.id.result);

        imgScanQR.setOnClickListener(this);
        int x = 9000;                                       // the ort number can be anything
        SQLiteOnWeb.init(this, x).start();

//        ShimmerTextView shimmerTextView = (ShimmerTextView) findViewById(R.id.shimmer_tv);
//
//        final Shimmer shimmer = new Shimmer();
//        shimmer.start(shimmerTextView);
    }


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null) {
                // Prevent duplicate scans
                return;
            } else {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //setting values to textviews
//                    txtResult.setText(obj.getString("type"));
                String type = null;
                try {
                    type = obj.getString("type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cardType = type;
                String number = null;
                try {
                    number = obj.getString("number");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "type: " + type + " number: " + number);

                int idDB = 0;
                if (tabPos == 0) {
                    idDB = clubInd + 1;       // check here if +1 is even required
                }
                if (tabPos == 1) {
                    idDB = diaInd + 1;       // check here if +1 is even required
                }
                if (tabPos == 2) {
                    idDB = heartInd + 1;       // check here if +1 is even required
                }
                if (tabPos == 3) {
                    idDB = spadeInd + 1;       // check here if +1 is even required
                }

//                db.addScannedQR(idDB, type, number, tabPos);        // the scanned QR is now added in the DB

                if (tabPos == 0) {
                    int flag = 0;
                    for (int i = 0; i < clubInd; i++) {
                        if (clubsArr.get(i).second.equals(number) && clubsArr.get(i).first.equals(type)) {
//                            Toast.makeText(MainActivity.this, "Card already present", Toast.LENGTH_SHORT).show();
                            flag = 1;
                            break;
                        }
                    }

                    if (flag == 0) {
                        Pair<String, String> pair = new Pair<>(type, number);
                        clubsArr.add(clubInd++, pair);
                        Log.d(TAG, "MainAct: " + "club++: " + clubInd);
                        PageAdapter adapter = new PageAdapter
                                (getSupportFragmentManager(), tabLayout.getTabCount());
                        Clubs.refresh();
//                        adapter.notifyDataSetChanged();
//
//                        viewPager.setAdapter(adapter);
//                        tabLayout.getTabAt(0).setText("Clubs");
//                        tabLayout.getTabAt(1).setText("Diamonds");
//                        tabLayout.getTabAt(2).setText("Hearts");
//                        tabLayout.getTabAt(3).setText("Spades");
                    }
                }

                if (tabPos == 1) {
//                    if (type.equals("diamond")) {
                    int flag = 0;
                    for (int i = 0; i < diaInd; i++) {
                        if (diamondsArr.get(i).second.equals(number) && diamondsArr.get(i).first.equals(type)) {
//                            Toast.makeText(MainActivity.this, "Card already present", Toast.LENGTH_SHORT).show();
                            flag = 1;
                            break;
                        }
                    }

                    if (flag == 0) {
                        Pair<String, String> pair = new Pair<>(type, number);
                        diamondsArr.add(diaInd++, pair);
                        //                            PageAdapter adapter = new PageAdapter
//                                    (getSupportFragmentManager(), tabLayout.getTabCount());
                        Diamonds.refresh();
//                        adapter.notifyDataSetChanged();

//                        viewPager.setAdapter(adapter);
//                        tabLayout.getTabAt(0).setText("Clubs");
//                        tabLayout.getTabAt(1).setText("Diamonds");
//                        tabLayout.getTabAt(2).setText("Hearts");
//                        tabLayout.getTabAt(3).setText("Spades");
                    }
                }

                if (tabPos == 2) {

//                    if (type.equals("heart")) {
                    int flag = 0;
                    for (int i = 0; i < heartInd; i++) {
                        if (heartsArr.get(i).second.equals(number) && heartsArr.get(i).first.equals(type)) {
//                            Toast.makeText(MainActivity.this, "Card already present", Toast.LENGTH_SHORT).show();
                            flag = 1;
                            break;
                        }
                    }

                    if (flag == 0) {
                        Pair<String, String> pair = new Pair<>(type, number);
                        heartsArr.add(heartInd++, pair);
//                            PageAdapter adapter = new PageAdapter
//                                    (getSupportFragmentManager(), tabLayout.getTabCount());
                        Hearts.refresh();
//                        adapter.notifyDataSetChanged();
//
////                            adapter.notifyDataSetChanged();
//                        viewPager.setAdapter(adapter);
//                        tabLayout.getTabAt(0).setText("Clubs");
//                        tabLayout.getTabAt(1).setText("Diamonds");
//                        tabLayout.getTabAt(2).setText("Hearts");
//                        tabLayout.getTabAt(3).setText("Spades");
                    }
                    Log.d(TAG, "heartsMain: " + heartsArr.size());
                }

                if (tabPos == 3) {
//                    if (type.equals("spade")) {
                    int flag = 0;
                    for (int i = 0; i < spadeInd; i++) {
                        if (spadesArr.get(i).second.equals(number) && spadesArr.get(i).first.equals(type)) {
//                            Toast.makeText(MainActivity.this, "Card already present", Toast.LENGTH_SHORT).show();
                            flag = 1;
                            break;
                        }
                    }

                    if (flag == 0) {
                        Pair<String, String> pair = new Pair<>(type, number);
                        spadesArr.add(spadeInd++, pair);
//                            PageAdapter adapter = new PageAdapter
//                                    (getSupportFragmentManager(), tabLayout.getTabCount());
                        Spades.refresh();
//                        adapter.notifyDataSetChanged();
//                        viewPager.setAdapter(adapter);
//                        tabLayout.getTabAt(0).setText("Clubs");
//                        tabLayout.getTabAt(1).setText("Diamonds");
//                        tabLayout.getTabAt(2).setText("Hearts");
//                        tabLayout.getTabAt(3).setText("Spades");
                    }
                }

//                Toast.makeText(MainActivity.this, "Card Scanned, Move to next card", Toast.LENGTH_SHORT).show();
//                integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
////                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setPrompt("Scan something");
////                    integrator.setOrientationLocked(false);
////                    integrator.setBeepEnabled(false);
//                integrator.initiateScan();
            }
        }

//                Toast.makeText(MainActivity.this, result.getText().toString(), Toast.LENGTH_SHORT).show();
//                barcodeView.setStatusText(result.getText());

        //Added preview of scanned barcode

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
////        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            //if qrcode has nothing in it
//
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
//                //if qr contains data
//                try {
//                    //converting the data to json
//
////                    >>>>>>>>>>>>>>>>>>   AVOID REPEATITION HERE  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//
//                    JSONObject obj = new JSONObject(result.getContents());
//                    //setting values to textviews
////                    txtResult.setText(obj.getString("type"));
//                    String type = obj.getString("type");
//                    cardType = type;
//                    String number = obj.getString("number");
//
//                    Log.d(TAG, "type: " + type + " number: " + number);
//
//                    int idDB = 0;
//                    if (tabPos == 0) {
//                        idDB = clubInd + 1;       // check here if +1 is even required
//                    }
//                    if (tabPos == 1) {
//                        idDB = diaInd + 1;       // check here if +1 is even required
//                    }
//                    if (tabPos == 2) {
//                        idDB = heartInd + 1;       // check here if +1 is even required
//                    }
//                    if (tabPos == 3) {
//                        idDB = spadeInd + 1;       // check here if +1 is even required
//                    }
//
//
//                    db.addScannedQR(idDB, type, number, tabPos);        // the scanned QR is now added in the DB
//
//                    if (tabPos == 0) {
//                        int flag = 0;
//                        for (int i = 0; i < clubInd; i++) {
//                            if (clubsArr.get(i).second.equals(number)) {
//                                Toast.makeText(this, "Card already present", Toast.LENGTH_SHORT).show();
//                                flag = 1;
//                                break;
//                            }
//                        }
//
//                        if (flag == 0) {
//                            Pair<String, String> pair = new Pair<>(type, number);
//                            clubsArr.add(clubInd++, pair);
//                            Log.d(TAG, "MainAct: " + "club++: " + clubInd);
////                            PageAdapter adapter = new PageAdapter
////                                    (getSupportFragmentManager(), tabLayout.getTabCount());
//                            adapter.notifyDataSetChanged();
//
//                            viewPager.setAdapter(adapter);
//                            tabLayout.getTabAt(0).setText("Clubs");
//                            tabLayout.getTabAt(1).setText("Diamonds");
//                            tabLayout.getTabAt(2).setText("Hearts");
//                            tabLayout.getTabAt(3).setText("Spades");
//                        }
//                    }
//
//                    if (tabPos == 1) {
////                    if (type.equals("diamond")) {
//                        int flag = 0;
//                        for (int i = 0; i < diaInd; i++) {
//                            if (diamondsArr.get(i).second.equals(number)) {
//                                Toast.makeText(this, "Card already present", Toast.LENGTH_SHORT).show();
//                                flag = 1;
//                                break;
//                            }
//                        }
//
//                        if (flag == 0) {
//                            Pair<String, String> pair = new Pair<>(type, number);
//                            diamondsArr.add(diaInd++, pair);
//                            //                            PageAdapter adapter = new PageAdapter
////                                    (getSupportFragmentManager(), tabLayout.getTabCount());
//                            adapter.notifyDataSetChanged();
//
//                            viewPager.setAdapter(adapter);
//                            tabLayout.getTabAt(0).setText("Clubs");
//                            tabLayout.getTabAt(1).setText("Diamonds");
//                            tabLayout.getTabAt(2).setText("Hearts");
//                            tabLayout.getTabAt(3).setText("Spades");
//                        }
//                    }
//
//                    if (tabPos == 2) {
//
////                    if (type.equals("heart")) {
//                        int flag = 0;
//                        for (int i = 0; i < heartInd; i++) {
//                            if (heartsArr.get(i).second.equals(number)) {
//                                Toast.makeText(this, "Card already present", Toast.LENGTH_SHORT).show();
//                                flag = 1;
//                                break;
//                            }
//                        }
//
//                        if (flag == 0) {
//                            Pair<String, String> pair = new Pair<>(type, number);
//                            heartsArr.add(heartInd++, pair);
////                            PageAdapter adapter = new PageAdapter
////                                    (getSupportFragmentManager(), tabLayout.getTabCount());
//                            adapter.notifyDataSetChanged();
//
////                            adapter.notifyDataSetChanged();
//                            viewPager.setAdapter(adapter);
//                            tabLayout.getTabAt(0).setText("Clubs");
//                            tabLayout.getTabAt(1).setText("Diamonds");
//                            tabLayout.getTabAt(2).setText("Hearts");
//                            tabLayout.getTabAt(3).setText("Spades");
//                        }
//                        Log.d(TAG, "heartsMain: " + heartsArr.size());
//                    }
//
//                    if (tabPos == 3) {
////                    if (type.equals("spade")) {
//                        int flag = 0;
//                        for (int i = 0; i < spadeInd; i++) {
//                            if (spadesArr.get(i).second.equals(number)) {
//                                Toast.makeText(this, "Card already present", Toast.LENGTH_SHORT).show();
//                                flag = 1;
//                                break;
//                            }
//                        }
//
//                        if (flag == 0) {
//                            Pair<String, String> pair = new Pair<>(type, number);
//                            spadesArr.add(spadeInd++, pair);
////                            PageAdapter adapter = new PageAdapter
////                                    (getSupportFragmentManager(), tabLayout.getTabCount());
//                            adapter.notifyDataSetChanged();
//                            viewPager.setAdapter(adapter);
//                            tabLayout.getTabAt(0).setText("Clubs");
//                            tabLayout.getTabAt(1).setText("Diamonds");
//                            tabLayout.getTabAt(2).setText("Hearts");
//                            tabLayout.getTabAt(3).setText("Spades");
//                        }
//                    }
//
//                    Toast.makeText(this, "Card Scanned, Move to next card", Toast.LENGTH_SHORT).show();
//                    integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
////                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                    integrator.setPrompt("Scan something");
////                    integrator.setOrientationLocked(false);
////                    integrator.setBeepEnabled(false);
//                    integrator.initiateScan();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    //if control comes here
//                    //that means the encoded format not matches
//                    //in this case you can display whatever data is available on the qrcode
//                    //to a toast
//                    Log.d(TAG, "error: " + e.getMessage());
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                }
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    void controlVisBAR() {
        barcodeView.setVisibility(View.VISIBLE);
        imgGrad.setVisibility(View.GONE);
        imgScanQR.setVisibility(View.GONE);
    }

    void controlVisCAM() {
        barcodeView.setVisibility(View.GONE);
        imgGrad.setVisibility(View.VISIBLE);
        imgScanQR.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        //initiating the qr code scan

        controlVisBAR();
        barcodeView.decodeContinuous(callback);
//        barcodeView.resume();

        barcodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlVisCAM();
            }
        });
    }
}