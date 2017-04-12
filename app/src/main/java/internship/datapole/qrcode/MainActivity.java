package internship.datapole.qrcode;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActvity";
    ImageView imgScanQR;
    TextView txtResult;
    private IntentIntegrator qrScan;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public static List<Pair<String, String>> clubsArr;
    public static List<Pair<String, String>> diamondsArr;
    public static List<Pair<String, String>> heartsArr;
    public static List<Pair<String, String>> spadesArr;

    public static String[] cards = new String[]{"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "Jack", "Queen", "King"};

//    public static Integer clubsArr[];
//    public static Integer diamondsArr[];
//    public static Integer heartsArr[];
//    public static Integer spadesArr[];

    public static Integer clubInd = 0;
    public static Integer diaInd = 0;
    public static Integer heartInd = 0;
    public static Integer spadeInd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        final PageAdapter adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Clubs");
        tabLayout.getTabAt(1).setText("Diamonds");
        tabLayout.getTabAt(2).setText("Hearts");
        tabLayout.getTabAt(3).setText("Spades");

        qrScan = new IntentIntegrator(this);

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
                        Pair<String, String> pair = new Pair<>(type, number);
                        clubsArr.add(clubInd++, pair);
                        PageAdapter adapter = new PageAdapter
                                (getSupportFragmentManager(), tabLayout.getTabCount());

                        viewPager.setAdapter(adapter);
                        tabLayout.getTabAt(0).setText("Clubs");
                        tabLayout.getTabAt(1).setText("Diamonds");
                        tabLayout.getTabAt(2).setText("Hearts");
                        tabLayout.getTabAt(3).setText("Spades");
                    }
                    if (type.equals("heart")) {
                        Pair<String, String> pair = new Pair<>(type, number);
                        heartsArr.add(heartInd++, pair);
                        PageAdapter adapter = new PageAdapter
                                (getSupportFragmentManager(), tabLayout.getTabCount());

                        viewPager.setAdapter(adapter);
                        tabLayout.getTabAt(0).setText("Clubs");
                        tabLayout.getTabAt(1).setText("Diamonds");
                        tabLayout.getTabAt(2).setText("Hearts");
                        tabLayout.getTabAt(3).setText("Spades");
                    }
                    if (type.equals("spade")) {
                        Pair<String, String> pair = new Pair<>(type, number);
                        spadesArr.add(spadeInd++, pair);
                        PageAdapter adapter = new PageAdapter
                                (getSupportFragmentManager(), tabLayout.getTabCount());

                        viewPager.setAdapter(adapter);
                        tabLayout.getTabAt(0).setText("Clubs");
                        tabLayout.getTabAt(1).setText("Diamonds");
                        tabLayout.getTabAt(2).setText("Hearts");
                        tabLayout.getTabAt(3).setText("Spades");
                    }
                    if (type.equals("diamond")) {
                        Pair<String, String> pair = new Pair<>(type, number);
                        diamondsArr.add(diaInd++, pair);

                        PageAdapter adapter = new PageAdapter
                                (getSupportFragmentManager(), tabLayout.getTabCount());

                        viewPager.setAdapter(adapter);
                        tabLayout.getTabAt(0).setText("Clubs");
                        tabLayout.getTabAt(1).setText("Diamonds");
                        tabLayout.getTabAt(2).setText("Hearts");
                        tabLayout.getTabAt(3).setText("Spades");
                    }

                    Toast.makeText(this, "Card Scanned, Move to next card", Toast.LENGTH_SHORT).show();
                    qrScan.initiateScan();
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

    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}
