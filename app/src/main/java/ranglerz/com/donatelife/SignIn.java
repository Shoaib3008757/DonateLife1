package ranglerz.com.donatelife;
// ********* By Shoaib Anwar *************** //
import android.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class SignIn extends AppCompatActivity {

    EditText m_mobileNumber, m_password, m_passwordAgain;
    Button m_submit, m_bt_savePassword;

    AlertDialog.Builder alertDialogBuilder;
    private ProgressBar bar;

    String URLRESULT;
    String m_number;
    String myFlag;
    boolean statusKey = false;
    private static final String TAG_NUMBER = "checknumber";
    private static final String TAG_SAVEPASS = "savepass";

    String bloodGroup, city;

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

    SharedPreferences sharedPreferences;


    String myJSON;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
        submitClick();
        savePassClick();
    }

    public void init(){


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SignIn.this ,R.color.colorRed)));
        getSupportActionBar().setTitle("Sign In");

        m_mobileNumber = (EditText) findViewById(R.id.m_ed_enter_mobil);
        m_password = (EditText) findViewById(R.id.m_ed_password);
        m_passwordAgain = (EditText) findViewById(R.id.m_ed_password_again);

        m_submit = (Button) findViewById(R.id.m_bt_sumbmit);
        m_bt_savePassword = (Button) findViewById(R.id.m_bt_createPassword);
        alertDialogBuilder = new AlertDialog.Builder(this);
        bar = (ProgressBar) this.findViewById(R.id.progressBar);
        myFlag = null;



    }

    public void submitClick(){
        m_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_mobileNumber.getVisibility()== View.VISIBLE){
                    m_number =  m_mobileNumber.getText().toString();

                    if (m_number.length()<10){
                        Toast.makeText(SignIn.this, "Please Enter Valid Phone", Toast.LENGTH_SHORT).show();
                    }else {
                        SignIn.AsyncDataClass asyncRequestObject = new SignIn.AsyncDataClass();
                        asyncRequestObject.execute(serverUrl, m_number);
                        m_number =  m_mobileNumber.getText().toString();

                        myFlag = TAG_NUMBER;
                    }
                }


            }
        });

    }

    public void savePassClick(){
        m_bt_savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_passwordAgain.getVisibility()==View.VISIBLE && m_passwordAgain.getVisibility()==View.VISIBLE);
                String pass = m_password.getText().toString();
                String passAgain = m_passwordAgain.getText().toString();

                if (pass.length()==0 || passAgain.length()==0){
                    Toast.makeText(SignIn.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }else if (pass.length()<=3){
                    Toast.makeText(SignIn.this, "Password Must be atleast 4 charecters lenght", Toast.LENGTH_SHORT).show();
                }

                else if (!pass.equals(passAgain)){
                    Toast.makeText(SignIn.this, "Password must be same", Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("TAG", "SSSSS " + m_number);
                    Log.e("TAG", "SSSSS " + pass);
                    SignIn.AsyncDataClass asyncRequestObject = new SignIn.AsyncDataClass();
                    asyncRequestObject.execute(serverUrl, m_number, pass);
                    myFlag = TAG_SAVEPASS;
                }
            }
        });
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


                if (myFlag.equals(TAG_NUMBER)) {

                    nameValuePairs.add(new BasicNameValuePair("mnumber", params[1]));
                }

                if (myFlag.equals(TAG_SAVEPASS)){
                    nameValuePairs.add(new BasicNameValuePair("mnumber", params[1]));
                    nameValuePairs.add(new BasicNameValuePair("password", params[2]));
                }

                if (statusKey){

                    nameValuePairs.add(new BasicNameValuePair("mnumber", params[1]));
                    nameValuePairs.add(new BasicNameValuePair("active", params[2]));
                }



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

                Toast.makeText(SignIn.this, "Server connection failed",
                        Toast.LENGTH_LONG).show();

                return;

            }




            int jsonResult = returnParsedJsonObject(result);

            Log.e("TAG", "VVVVVVV " + result);


            String ress = returnParsedJsonObjectString(result);

            Log.e("TAGE", "VVVVVVV 1 1 1 " + ress);

            if (ress!=null){
            if (ress.equals("1")){
                Toast.makeText(SignIn.this, "Your Status Updated...", Toast.LENGTH_SHORT).show();
                finish();
            }
            }

            Log.e("TAGE", "VVVVVVV122 " + jsonResult);
            URLRESULT = result;
            showList(result);



            if(jsonResult == 1){

                bar.setVisibility(View.GONE);

                if (myFlag.equals(TAG_NUMBER)) {
                    Toast.makeText(SignIn.this, "You are Registered", Toast.LENGTH_SHORT).show();
                    m_mobileNumber.setVisibility(View.GONE);
                    m_password.setVisibility(View.VISIBLE);
                    m_passwordAgain.setVisibility(View.VISIBLE);
                    m_submit.setVisibility(View.GONE);
                    m_bt_savePassword.setVisibility(View.VISIBLE);

                    int mobileNumberToSave = Integer.parseInt(m_number);
                    Log.e("TAGE", "mobile number " + mobileNumberToSave);
                    sharedPreferences = getSharedPreferences("mobile", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putString("number", m_number);
                    editor.commit();

                }
                if (myFlag.equals(TAG_SAVEPASS)){
                    Toast.makeText(SignIn.this, "Password Saved Successfully", Toast.LENGTH_SHORT).show();
                    finish();
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
            if (myFlag.equals(TAG_SAVEPASS)){
                returnedResult = resultObject.getInt("success");
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
                Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show();

                //showing diloag if not registered
                Toast.makeText(SignIn.this, "You are not Registered", Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.GONE);
                AlertDialog.Builder alert = new AlertDialog.Builder(SignIn.this);
                alert.setTitle("Your Number is Not Registered");
                alert.setMessage("Press Ok for registeration!");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent registeryourbloodactvity = new Intent(SignIn.this, RegisterYourBlood.class);
                        startActivity(registeryourbloodactvity);
                        finish();
                    }
                });
                alert.show();

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


                    final Dialog dialog = new Dialog(SignIn.this);
                    dialog.setContentView(R.layout.status_dialog);



                    final TextView d_tv_status;
                    final Switch swButton;
                    Button d_button_ok;



                    swButton = (Switch) dialog.findViewById(R.id.d_button_switch);
                    d_button_ok = (Button) dialog.findViewById(R.id.d_button_ok);
                    d_tv_status = (TextView) dialog.findViewById(R.id.tv_status);

                    dialog.getWindow().setBackgroundDrawableResource(R.color.colorRed);
                    dialog.setTitle(user_name);



                    swButton.setChecked(false);

                    d_tv_status.setText("DE-ACTIVE");

                    swButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                d_tv_status.setText("ACTIVE DONOR ");
                            }else {
                                d_tv_status.setText("DE-ACTIVE DONOR ");
                            }
                        }
                    });


                    d_button_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isChecked = swButton.isChecked();
                            Log.e("TAG", "STATUS FOR SWTICH: " + isChecked);

                            if (isChecked){



                                SignIn.AsyncDataClass asyncRequestObject = new SignIn.AsyncDataClass();
                                asyncRequestObject.execute(serverUrl, mobile_number, "no");

                                dialog.dismiss();


                            }
                            if (!isChecked){
                                SignIn.AsyncDataClass asyncRequestObject = new SignIn.AsyncDataClass();
                                asyncRequestObject.execute(serverUrl, mobile_number, "yes");
                                dialog.dismiss();
                            }


                        }
                    });

                    dialog.show();
                    sharedPreferences = getSharedPreferences("mobile", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putString("number", m_number);
                    editor.commit();



                }

                if (active.equals("no")&&!password.equals("null")){

                    final Dialog dialog = new Dialog(SignIn.this);
                    dialog.setContentView(R.layout.status_dialog);


                    TextView d_title, dBloodGroup, dAge, dFrom;
                    final Switch swButton;
                    Button d_button_ok;
                    final TextView d_tv_status;


                    swButton = (Switch) dialog.findViewById(R.id.d_button_switch);
                    d_button_ok = (Button) dialog.findViewById(R.id.d_button_ok);
                    d_tv_status = (TextView) dialog.findViewById(R.id.tv_status);


                    dialog.getWindow().setBackgroundDrawableResource(R.color.colorRed);
                    dialog.setTitle(user_name);


                    swButton.setChecked(true);
                    d_tv_status.setText("ACTIVE DONOR");

                    swButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                d_tv_status.setText("ACTIVE");
                            }else {
                                d_tv_status.setText("DE-ACTIVE");
                            }
                        }
                    });


                    d_button_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isChecked = swButton.isChecked();
                            Log.e("TAG", "STATUS FOR SWTICH: " + isChecked);

                            if (isChecked){

                                SignIn.AsyncDataClass asyncRequestObject = new SignIn.AsyncDataClass();
                                asyncRequestObject.execute(serverUrl, mobile_number, "no");
                                dialog.dismiss();


                            }
                            if (!isChecked){
                                SignIn.AsyncDataClass asyncRequestObject = new SignIn.AsyncDataClass();
                                asyncRequestObject.execute(serverUrl, mobile_number, "yes");

                                dialog.dismiss();
                            }


                        }
                    });

                    dialog.show();

                    //adding number to share preferences
                    sharedPreferences = getSharedPreferences("mobile", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putString("number", m_number);
                    editor.commit();



                }


                if (password.equals("null")){

                    Toast.makeText(this, "Please Create A Password", Toast.LENGTH_SHORT).show();
                    m_mobileNumber.setVisibility(View.GONE);
                    m_password.setVisibility(View.VISIBLE);
                    m_passwordAgain.setVisibility(View.VISIBLE);
                    m_submit.setVisibility(View.GONE);
                    m_bt_savePassword.setVisibility(View.VISIBLE);


                }
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
}
