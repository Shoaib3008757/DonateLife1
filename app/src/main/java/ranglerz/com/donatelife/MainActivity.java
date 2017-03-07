package ranglerz.com.donatelife;
// ********* By Shoaib Anwar *************** //
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RelativeLayout searchBlood, registerBlood;
    ImageView optionDots;

    SharedPreferences sharedPreferences;
    private ProgressBar bar;
    int userStatus = 0;




    String myJSON;
    String URLRESULT;
    String myFlag;
    boolean statusKey = false;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    private static final String TAG_NUMBER = "checknumber";

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_MOBILE_NUMBER = "mnumber";
    private static final String TAG_BLOODGROUP = "bloodgroup";
    private static final String TAG_CITY = "city";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_dob = "dob";
    private static final String TAG_PASS = "password";
    private static final String TAGE_ACTIVE = "active";


    //private final String serverUrl = "http://fun4yu.atwebpages.com/indexbloodtable.php";
    private final String serverUrl = "http://donate-life.ranglerz.be/indexbloodtable.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //customOptionView();
        buttonListenere();


    }//end of onCreate

    public void init(){
        searchBlood = (RelativeLayout) findViewById(R.id.button_search_blood);
        registerBlood = (RelativeLayout) findViewById(R.id.button_register_blood);
        optionDots = (ImageView) findViewById(R.id.iv_dots);

        bar = (ProgressBar) this.findViewById(R.id.progressBar);

        myFlag = null;

        //initializatino for admob
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2735690748218549/3006880912");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        myFlag = TAG_NUMBER;

    }

    public void buttonListenere(){
        //search button listenere
        searchBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchForBlood.class);
                startActivity(i);
            }
        });//ending searchBlood listener

        //register button listener
        registerBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Intent i = new Intent(MainActivity.this, RegisterYourBlood.class);
                startActivity(i);
            }
        });//end registerBlodd listener
    }//end of button listner button


    public void customOptionView(){
        optionDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (userStatus == 1) {
                    PopupMenu popup = new PopupMenu(MainActivity.this, optionDots);
                    Context wrapper = new ContextThemeWrapper(MainActivity.this, R.style.PopupMenu);
                    final PopupMenu popupMenu = new PopupMenu(wrapper, optionDots);
                    //Inflating the Popup using xml file
                    popupMenu.getMenuInflater().inflate(R.menu.sign_in, popupMenu.getMenu());


                    //registering popup with OnMenuItemClickListener
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent signInActvity = new Intent(MainActivity.this, SignIn.class);
                            startActivity(signInActvity);
                            return true;
                        }
                    });

                    popupMenu.show();//showing popup menu

                }

                if (userStatus == 2) {
                    PopupMenu popup = new PopupMenu(MainActivity.this, optionDots);
                    Context wrapper = new ContextThemeWrapper(MainActivity.this, R.style.PopupMenu);
                    final PopupMenu popupMenu = new PopupMenu(wrapper, optionDots);
                    //Inflating the Popup using xml file
                    popupMenu.getMenuInflater().inflate(R.menu.sign_out, popupMenu.getMenu());


                    //registering popup with OnMenuItemClickListener
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent signInActvity = new Intent(MainActivity.this, SignIn.class);
                            startActivity(signInActvity);
                            return true;
                        }
                    });

                    popupMenu.show();//showing popup menu

                }


                if (userStatus == 3) {
                    PopupMenu popup = new PopupMenu(MainActivity.this, optionDots);
                    Context wrapper = new ContextThemeWrapper(MainActivity.this, R.style.PopupMenu);
                    final PopupMenu popupMenu = new PopupMenu(wrapper, optionDots);
                    //Inflating the Popup using xml file
                    popupMenu.getMenuInflater().inflate(R.menu.sign_menu, popupMenu.getMenu());


                    //registering popup with OnMenuItemClickListener
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent signInActvity = new Intent(MainActivity.this, SignIn.class);
                            startActivity(signInActvity);
                            return true;
                        }
                    });

                    popupMenu.show();//showing popup menu

                    searchBlood.setVisibility(View.VISIBLE);
                    registerBlood.setVisibility(View.VISIBLE);
                }


            }

        });
    }

    public void checkUserSingInStatus(){

        sharedPreferences = getSharedPreferences("mobile", 0);

        if (sharedPreferences!=null){
            String number = sharedPreferences.getString("number", null);
            Log.e("TAGE " , "Mobile Number " + number);
            if (number!=null){


                MainActivity.AsyncDataClass asyncRequestObject = new AsyncDataClass();
                asyncRequestObject.execute(serverUrl, number
                );


            }

            if (number==null){

                userStatus = 3;

            }


        }




    }//end for checkUserSignInStatus


    @Override
    protected void onResume() {
        super.onResume();


        customOptionView();
        checkUserSingInStatus();


    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override

        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);

            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);

            HttpPost httpPost = new HttpPost(params[0]);


            String jsonResult = "";

            try {



                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);




                    nameValuePairs.add(new BasicNameValuePair("mnumber", params[1]));




                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();



                Log.d("TAG", "SSSSSSS " + jsonResult);






            } catch (ClientProtocolException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }

            return jsonResult;

        }

        @Override

        protected void onPreExecute() {

            super.onPreExecute();



            bar.setVisibility(View.VISIBLE);
        }

        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            bar.setVisibility(View.GONE);



            if(result.equals("") || result == null){

                Toast.makeText(MainActivity.this, "Server connection failed",
                        Toast.LENGTH_LONG).show();

                return;

            }




            int jsonResult = returnParsedJsonObject(result);

            Log.e("TAG", "VVVVVVV " + result);


            String ress = returnParsedJsonObjectString(result);

            Log.e("TAGE", "VVVVVVV 1 1 1 " + ress);

            if (ress!=null){
                if (ress.equals("1")){
                    Toast.makeText(MainActivity.this, "Your Status Updated...", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            Log.e("TAGE", "VVVVVVV122 " + jsonResult);
            URLRESULT = result;
            showList(result);



            if(jsonResult == 1){

               bar.setVisibility(View.GONE);

                if (myFlag.equals(TAG_NUMBER)) {
                    Toast.makeText(MainActivity.this, "You are Registered", Toast.LENGTH_SHORT).show();


                }


                //do all fatch result funtion here

                // getData();

            }

            myJSON=URLRESULT;

        }

        private StringBuilder inputStreamToString(InputStream is) {

            String rLine = "";

            StringBuilder answer = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {

                while ((rLine = br.readLine()) != null) {

                    answer.append(rLine);

                }

            } catch (IOException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            }

            return answer;

        }

    }

    private int returnParsedJsonObject(String result){

        JSONObject resultObject = null;

        int returnedResult = 0;

        try {

            resultObject = new JSONObject(result);

            if (myFlag.equals(TAG_NUMBER)){
                returnedResult = resultObject.getInt("already");
            }
            if (statusKey==true){

                returnedResult = resultObject.getInt("status");

            }
        } catch (JSONException e) {

            e.printStackTrace();

        }

        return returnedResult;

    }


    private String returnParsedJsonObjectString(String result){

        JSONObject resultObject = null;

        String returnedResult = null;

        try {

            resultObject = new JSONObject(result);



            returnedResult = resultObject.getString("status");


        } catch (JSONException e) {

            e.printStackTrace();

        }

        return returnedResult;

    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void showList(String result){
        try {

            JSONObject jsonObj = new JSONObject(result);
            Log.e("TAGE", "RESULT " + result.toString());
            String rel =  jsonObj.getString(TAG_RESULTS);
            Log.e("TAGE", "RESULT 123 " + rel);



            if (rel.equals("null")){
                Log.e("TAG", "SSSSTT Emtpy");
                Toast.makeText(this, "No Record Found ", Toast.LENGTH_SHORT).show();



            }else {

                peoples = jsonObj.getJSONArray(TAG_RESULTS);

                JSONObject c1 = peoples.getJSONObject(0);
                final String mobile_number = c1.getString(TAG_MOBILE_NUMBER);
                String user_name = c1.getString(TAG_NAME);
                String password = c1.getString(TAG_PASS);
                String active = c1.getString(TAGE_ACTIVE);

                Log.e("TAG", "MOBILE " + mobile_number);
                Log.e("TAG", "MOBILE " + user_name);
                Log.e("TAG", "PASSWORD " + password);
                Log.e("TAG", "ACTIVE " + active);




                statusKey = true;

                if (active.equals("yes")&&!password.equals("null")){

                   // Toast.makeText(this, "Donor is sign In", Toast.LENGTH_SHORT).show();
                    //d_tv_status.setText("ACTIVE DONOR");



                    searchBlood.setVisibility(View.VISIBLE);
                    registerBlood.setVisibility(View.GONE);

                    userStatus = 1;


                }

                if (active.equals("no")&&!password.equals("null")){

                    //d_tv_status.setText("DE-ACTIVE");

                   // Toast.makeText(this, "Donor is sign out", Toast.LENGTH_SHORT).show();


                    searchBlood.setVisibility(View.VISIBLE);
                    registerBlood.setVisibility(View.GONE);

                    userStatus = 2;

                }


                if (password.equals("null")){

                    //Toast.makeText(this, "No Status for Donor Create Password ", Toast.LENGTH_SHORT).show();

                    userStatus = 3;



                }
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

}
