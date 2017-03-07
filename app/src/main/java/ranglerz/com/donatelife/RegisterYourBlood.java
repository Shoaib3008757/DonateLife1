package ranglerz.com.donatelife;
// ********* By Shoaib Anwar *************** //
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterYourBlood extends AppCompatActivity{


    protected String enteredUsername;

    private DatePickerDialog datePictkerDialog;
    private SimpleDateFormat dateFormatter;

    ProgressDialog progresDialog;
    private ProgressBar bar;

    //private final String serverUrl = "http://fun4yu.atwebpages.com/indexbloodtable.php";
    private final String serverUrl = "http://donate-life.ranglerz.be/indexbloodtable.php";


    EditText registringName, registeringPhon, registringDataOfBirth;
    Spinner bloodGroup, city;
    RelativeLayout submiit;
    RadioButton radioMale, radioFemale;
    RadioGroup maleOrFemale;

    boolean isGenderSelected;

    String spinerGender, spinerBloodGroup, spinnerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_your_blood);

        init();

        submitButtonClickListener();
        datePictureDilaogOnEditTextDOB();



    }

    public void init(){
        registringName = (EditText) findViewById(R.id.r_name);
        registeringPhon = (EditText) findViewById(R.id.r_phone);
        registringDataOfBirth = (EditText) findViewById(R.id.r_birth_date);
       // gender = (Spinner) findViewById(R.id.r_spinner_gender);
        bloodGroup = (Spinner) findViewById(R.id.rr_spinner_blood_group);
        city = (Spinner) findViewById(R.id.r_spinner_cities);
        submiit = (RelativeLayout) findViewById(R.id.r_button_submit);
        radioMale = (RadioButton) findViewById(R.id.radio_male);
        radioFemale = (RadioButton) findViewById(R.id.radio_female);
        maleOrFemale = (RadioGroup) findViewById(R.id.radioGroupMaleFemale);

        progresDialog = new ProgressDialog(RegisterYourBlood.this);
        bar = (ProgressBar) this.findViewById(R.id.progressBar);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        spinerGender = "null";
        isGenderSelected = false;

        //ad code
        //initializatino for admob
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2735690748218549/3006880912");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void datePictureDilaogOnEditTextDOB(){
        registringDataOfBirth.setFocusable(false);
        registringDataOfBirth.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                datePictkerDialog = new DatePickerDialog(RegisterYourBlood.this, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        registringDataOfBirth.setText(dateFormatter.format(newDate.getTime()));
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


                datePictkerDialog.show();
                datePictkerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            }
        });
    }

    public void submitButtonClickListener(){
        submiit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name, phNumber, DOB;
                int spGender, spBGroup, spCity;
                name = registringName.getText().toString();
                phNumber = registeringPhon.getText().toString();
                DOB = registringDataOfBirth.getText().toString();
               // spGender = gender.getSelectedItemPosition();
                //Log.d("TAG", "ITEM POSITION: " + spGender);
                spBGroup = bloodGroup.getSelectedItemPosition();
                spCity = city.getSelectedItemPosition();

                spinerGender = "Not Provided";
                getSelectedMaleOrFemale();


                if (name.length()==0
                        || phNumber.length()==0
                        || DOB.length()==0){
                    Toast.makeText(RegisterYourBlood.this, "Fields Should not be Empty", Toast.LENGTH_SHORT).show();
                }

                else if (phNumber.length()<10){

                    Toast.makeText(RegisterYourBlood.this, "Phone Number is Not Valid", Toast.LENGTH_SHORT).show();
                }

              /*  else if (spGender==0){
                    Toast.makeText(RegisterYourBlood.this, "Please Select your Gender", Toast.LENGTH_SHORT).show();
                }*/

                else if (spBGroup==0){
                    Toast.makeText(RegisterYourBlood.this, "Please Select Your Blood Group", Toast.LENGTH_SHORT).show();
                }

                else if (spCity==0){
                    Toast.makeText(RegisterYourBlood.this, "Please Select your City", Toast.LENGTH_SHORT).show();
                }
                else if (isGenderSelected==false){

                    Toast.makeText(RegisterYourBlood.this, "Please Select Your Gender", Toast.LENGTH_SHORT).show();
                }
                else {
                    //spinerGender = gender.getSelectedItem().toString();




                    spinerBloodGroup = bloodGroup.getSelectedItem().toString();
                    spinnerCity = city.getSelectedItem().toString();



                    Log.e("TAG", "Values: " + name);
                    Log.e("TAG", "Values: " + phNumber);
                    Log.e("TAG", "Values: " + DOB);


                    Log.e("TAG", "Values: " + spinerGender);
                    Log.e("TAG", "Values: " + spinerBloodGroup);
                    Log.e("TAG", "Values: " + spinnerCity);

                    AsyncDataClass asyncRequestObject = new AsyncDataClass();

                    asyncRequestObject.execute(serverUrl, name, phNumber, spinerBloodGroup, spinnerCity, spinerGender, DOB);


                }

            }
        });
    }



    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override

        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, 8000);

            HttpConnectionParams.setSoTimeout(httpParameters, 8000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);

            HttpPost httpPost = new HttpPost(params[0]);

            String jsonResult = "";

            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("name", params[1]));

                nameValuePairs.add(new BasicNameValuePair("mnumber", params[2]));

                nameValuePairs.add(new BasicNameValuePair("bloodgroup", params[3]));

                nameValuePairs.add(new BasicNameValuePair("city", params[4]));

                nameValuePairs.add(new BasicNameValuePair("gender", params[5]));

                nameValuePairs.add(new BasicNameValuePair("dob", params[6]));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

                Log.e("TAG", "Resulted Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return jsonResult;

        }

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

            bar.setVisibility(View.VISIBLE);


        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);




            Log.e("TAG", "Resulted Value: " + result);

            if(result.equals("") || result == null){

                Toast.makeText(RegisterYourBlood.this, "Server connection failed", Toast.LENGTH_LONG).show();

                bar.setVisibility(View.GONE);
                if (progresDialog.isShowing()){
                    progresDialog.dismiss();
                }
                return;

            }

            int jsonResult = returnParsedJsonObject(result);

            if(jsonResult == 0){

                if (progresDialog.isShowing()){
                    progresDialog.dismiss();
                }

                Toast.makeText(RegisterYourBlood.this, "Mobile Number Already Registered", Toast.LENGTH_LONG).show();
                bar.setVisibility(View.GONE);


                return;

            }

            if(jsonResult == 1){

                Toast.makeText(RegisterYourBlood.this, "Register Successfully", Toast.LENGTH_LONG).show();

                /*Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                intent.putExtra("USERNAME", enteredUsername);

                intent.putExtra("MESSAGE", "You have been successfully Registered");

                startActivity(intent);*/

                bar.setVisibility(View.GONE);


                final Dialog dialog = new Dialog(RegisterYourBlood.this);
                dialog.setContentView(R.layout.welcome_dialog);


                TextView d_r_name;
                Button d_ok;

                d_r_name = (TextView)dialog.findViewById(R.id.d_user_name);
                d_ok = (Button)dialog.findViewById(R.id.d_button_ok);

                String rName =  registringName.getText().toString();
                d_r_name.setText("Welcome " + rName+"!");

                dialog.getWindow().setBackgroundDrawableResource(R.color.colorRed);
                dialog.setTitle("Registered Successfully");

                d_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent tipsActvity = new Intent(RegisterYourBlood.this, TipsForDonorsActvity.class);
                        startActivity(tipsActvity);
                        dialog.dismiss();
                        finish();
                    }
                });

                dialog.setCancelable(false);
                dialog.show();

                /*Intent tipsActvity = new Intent(RegisterYourBlood.this, TipsForDonorsActvity.class);
                startActivity(tipsActvity);
                finish();*/
            }



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

            returnedResult = resultObject.getInt("success");

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return returnedResult;

    }

    public boolean getSelectedMaleOrFemale() {

        isGenderSelected =  false;

        int selectedId = maleOrFemale.getCheckedRadioButtonId();
        Log.e("TAGE", "SELECTED VALUE: " + selectedId);

        if (selectedId == -1) {
            Log.e("TAGe", "Radio Button Not Selected");

        } else{

            // find the radiobutton by returned id
            RadioButton radioButton;
        radioButton = (RadioButton) findViewById(selectedId);

            String selectedMaleOrFemale = radioButton.getText().toString();
            spinerGender = selectedMaleOrFemale;

             isGenderSelected = true;

    }

        return isGenderSelected;

    }




}
