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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public static int tabPos;
//    public static Integer clubsArr[];
//    public static Integer diamondsArr[];
//    public static Integer heartsArr[];
//    public static Integer spadesArr[];

    public static Integer clubInd = 0;
    public static Integer diaInd = 0;
    public static Integer heartInd = 0;
    public static Integer spadeInd = 0;

    public PageAdapter adapter;
    private CompoundBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        barcodeView = (CompoundBarcodeView) findViewById(R.id.bar_code);

        integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan something");
//        integrator.setOrientationLocked(false);
//        integrator.setBeepEnabled(false);
//        integrator.initiateScan();

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
        integrator = new IntentIntegrator(this);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it

            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json

//                    >>>>>>>>>>>>>>>>>>   AVOID REPEATITION HERE  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<

                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
//                    txtResult.setText(obj.getString("type"));
                    String type = obj.getString("type");
                    String number = obj.getString("number");

                    Log.d(TAG, "type: " + type + " number: " + number);

                    if (type.equals("club")) {
                        int flag = 0;
                        for (int i = 0; i < clubInd; i++) {
                            if (clubsArr.get(i).second.equals(number)) {
                                Toast.makeText(this, "Card already present", Toast.LENGTH_SHORT).show();
                                flag = 1;
                                break;
                            }
                        }

                        if (flag == 0) {
                            Pair<String, String> pair = new Pair<>(type, number);
                            clubsArr.add(clubInd++, pair);
                            Log.d(TAG, "MainAct: " + "club++: " + clubInd);
//                            PageAdapter adapter = new PageAdapter
//                                    (getSupportFragmentManager(), tabLayout.getTabCount());
                            adapter.notifyDataSetChanged();

                            viewPager.setAdapter(adapter);
                            tabLayout.getTabAt(0).setText("Clubs");
                            tabLayout.getTabAt(1).setText("Diamonds");
                            tabLayout.getTabAt(2).setText("Hearts");
                            tabLayout.getTabAt(3).setText("Spades");
                        }
                    }
                    if (type.equals("heart")) {
                        int flag = 0;
                        for (int i = 0; i < heartInd; i++) {
                            if (heartsArr.get(i).second.equals(number)) {
                                Toast.makeText(this, "Card already present", Toast.LENGTH_SHORT).show();
                                flag = 1;
                                break;
                            }
                        }

                        if (flag == 0) {
                            Pair<String, String> pair = new Pair<>(type, number);
                            heartsArr.add(heartInd++, pair);
//                            PageAdapter adapter = new PageAdapter
//                                    (getSupportFragmentManager(), tabLayout.getTabCount());
                            adapter.notifyDataSetChanged();

//                            adapter.notifyDataSetChanged();
                            viewPager.setAdapter(adapter);
                            tabLayout.getTabAt(0).setText("Clubs");
                            tabLayout.getTabAt(1).setText("Diamonds");
                            tabLayout.getTabAt(2).setText("Hearts");
                            tabLayout.getTabAt(3).setText("Spades");
                        }
                        Log.d(TAG, "heartsMain: " + heartsArr.size());
                    }
                    if (type.equals("spade")) {
                        int flag = 0;
                        for (int i = 0; i < spadeInd; i++) {
                            if (spadesArr.get(i).second.equals(number)) {
                                Toast.makeText(this, "Card already present", Toast.LENGTH_SHORT).show();
                                flag = 1;
                                break;
                            }
                        }

                        if (flag == 0) {
                            Pair<String, String> pair = new Pair<>(type, number);
                            spadesArr.add(spadeInd++, pair);
//                            PageAdapter adapter = new PageAdapter
//                                    (getSupportFragmentManager(), tabLayout.getTabCount());
                            adapter.notifyDataSetChanged();
                            viewPager.setAdapter(adapter);
                            tabLayout.getTabAt(0).setText("Clubs");
                            tabLayout.getTabAt(1).setText("Diamonds");
                            tabLayout.getTabAt(2).setText("Hearts");
                            tabLayout.getTabAt(3).setText("Spades");
                        }
                    }
                    if (type.equals("diamond")) {
                        int flag = 0;
                        for (int i = 0; i < diaInd; i++) {
                            if (diamondsArr.get(i).second.equals(number)) {
                                Toast.makeText(this, "Card already present", Toast.LENGTH_SHORT).show();
                                flag = 1;
                                break;
                            }
                        }

                        if (flag == 0) {
                            Pair<String, String> pair = new Pair<>(type, number);
                            diamondsArr.add(diaInd++, pair);
                            //                            PageAdapter adapter = new PageAdapter
//                                    (getSupportFragmentManager(), tabLayout.getTabCount());
                            adapter.notifyDataSetChanged();

                            viewPager.setAdapter(adapter);
                            tabLayout.getTabAt(0).setText("Clubs");
                            tabLayout.getTabAt(1).setText("Diamonds");
                            tabLayout.getTabAt(2).setText("Hearts");
                            tabLayout.getTabAt(3).setText("Spades");
                        }
                    }

                    Toast.makeText(this, "Card Scanned, Move to next card", Toast.LENGTH_SHORT).show();
                    integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
//                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                    integrator.setPrompt("Scan something");
//                    integrator.setOrientationLocked(false);
//                    integrator.setBeepEnabled(false);
                    integrator.initiateScan();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Log.d(TAG, "error: " + e.getMessage());
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

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
        barcodeView.resume();

        barcodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlVisCAM();
            }
        });
    }
}