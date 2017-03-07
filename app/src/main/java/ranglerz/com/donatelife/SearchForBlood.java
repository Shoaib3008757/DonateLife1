package ranglerz.com.donatelife;
// ********* By Shoaib Anwar *************** //
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class SearchForBlood extends AppCompatActivity {

    Spinner spinnerCity, spinnerBloodGroup;
    RelativeLayout btSearch;
    ListView resultListView;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_MOBILE_NUMBER = "mnumber";
    private static final String TAG_BLOODGROUP = "bloodgroup";
    private static final String TAG_CITY = "city";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_dob = "dob";

    AlertDialog.Builder alertDialogBuilder;
    private ProgressBar bar;

    static final int MY_PERMISSIONS_PHONE_CALL = 1000;

     String mn = null;

    String URLRESULT;

    String bloodGroup, city;

    //private final String serverUrl = "http://fun4yu.atwebpages.com/indexbloodtable.php";
    private final String serverUrl = "http://donate-life.ranglerz.be/indexbloodtable.php";




    String myJSON;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_blood);

        init();
        btSearchClickHandler();

        showSingleResultOnListItemClick();
    }

    public void init() {
        spinnerCity = (Spinner) findViewById(R.id.spinner_search_city);
        spinnerBloodGroup = (Spinner) findViewById(R.id.spinner_search_blood_group);
        btSearch = (RelativeLayout) findViewById(R.id.button_serarch);
        resultListView = (ListView) findViewById(R.id.result_list_view);

        personList = new ArrayList<HashMap<String, String>>();
        alertDialogBuilder = new AlertDialog.Builder(this);
        bar = (ProgressBar) this.findViewById(R.id.progressBar);
    }

    public void btSearchClickHandler() {

        //setting click lister on serach button
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int spBloodGrouValue, spCitiyValue;


                spCitiyValue = spinnerCity.getSelectedItemPosition();
                spBloodGrouValue = spinnerBloodGroup.getSelectedItemPosition();

                /*if (spCitiyValue == 0) {
                    Toast.makeText(SearchForBlood.this, "Please Select City",
                            Toast.LENGTH_SHORT).show();
                }*/

                if (spBloodGrouValue == 0) {
                    Toast.makeText(SearchForBlood.this, "Please Select Blood Group",
                            Toast.LENGTH_SHORT).show();
                }else if (spCitiyValue!=0 || spBloodGrouValue!=0){
                    //do data pass here to server
                    city = spinnerCity.getSelectedItem().toString();
                    bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
                    Log.e("TAG", "Values: " + city);
                    Log.e("TAG", "Values: " + bloodGroup);

                    //getData();


                    if (!personList.isEmpty()) {
                        personList.clear();
                    }


                    AsyncDataClass asyncRequestObject = new AsyncDataClass();

                    if (bloodGroup.equals("Select Blood Group")) {
                        asyncRequestObject.execute(serverUrl, city);
                    }
                    if (city.equals("Select City")) {
                        asyncRequestObject.execute(serverUrl, bloodGroup);
                    }
                    if (!bloodGroup.equals("Select Blood Group") && !city.equals("Select City")) {
                        asyncRequestObject.execute(serverUrl, bloodGroup, city);
                    }


                    Toast.makeText(SearchForBlood.this, "Searching for Blood Match...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showSingleResultOnListItemClick() {
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String values = parent.getItemAtPosition(position).toString();
                String[] array = new String[]{values};

                TextView NAME = (TextView) view.findViewById(R.id.searched_name);
                TextView BLOODGROUP = (TextView) view.findViewById(R.id.searched_bloodgroup);
                TextView MOBILNUMBER = (TextView) view.findViewById(R.id.searched_mobile_number);
                TextView CITY = (TextView) view.findViewById(R.id.searched_city);
                TextView GENDER = (TextView) view.findViewById(R.id.searched_gender);
                TextView DOB = (TextView) view.findViewById(R.id.searched_dob);

                final String name = NAME.getText().toString();
                String bg = BLOODGROUP.getText().toString();
                 mn = MOBILNUMBER.getText().toString();
                String city = CITY.getText().toString();
                String gndr = GENDER.getText().toString();
                String dob = DOB.getText().toString();


                Log.e("TAGE", "ITEM CLICK " + values);
                Log.e("TAGE", "ITEM CLICK NAME: " + name);
                Log.e("TAGE", "ITEM CLICK BLOOD GROUP: " + bg);
                Log.e("TAGE", "ITEM CLICK MOBILE NUMBER: " + mn);
                Log.e("TAGE", "ITEM CLICK CITY: " + city);
                Log.e("TAGE", "ITEM CLICK GENDER: " + gndr);
                Log.e("TAGE", "ITEM CLICK DATH OF BIRTH: " + dob);



                final Dialog dialog = new Dialog(SearchForBlood.this);
                dialog.setContentView(R.layout.result_dialog);


                TextView d_title, dBloodGroup, dAge, dFrom;
                Button dCallNow, dCallLater;

                //d_title = (TextView)dialog.findViewById(R.id.dialog_title);
                dBloodGroup = (TextView)dialog.findViewById(R.id.d_blood);
                dAge = (TextView)dialog.findViewById(R.id.d_age);
                dFrom = (TextView)dialog.findViewById(R.id.d_from);

                dCallNow = (Button)dialog.findViewById(R.id.d_button_call_now);
                dCallLater = (Button)dialog.findViewById(R.id.d_button_call_later);


                dialog.getWindow().setBackgroundDrawableResource(R.color.colorRed);
                dialog.setTitle(name);





                //d_title.setText(name);
                dBloodGroup.setText(bg);
                dAge.setText(dob);
                dFrom.setText(city);

                dCallNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        call();

                    }
                });

                dCallLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();



            }
        });
    }

    public void call(){


            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_PHONE_CALL);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String mobb = mn;
                Log.e("TAG", "MMMMMMMMMM " + mobb);
                callIntent.setData(Uri.parse("tel:" + mobb));
                startActivity(callIntent);
            }



        ///////////////////

     /*   Intent call = new Intent(Intent.ACTION_CALL);
        String mobb = mn;
        //mobb.replaceAll(mn, name);
        Log.e("TAG", "MMMMMMMMMM " + mobb);
        call.setData(Uri.parse("tel:" + mobb));
        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(call);*/


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

                if (bloodGroup.equals("Select Blood Group")){

                    nameValuePairs.add(new BasicNameValuePair("city", params[1]));

                }

                if(city.equals("Select City")){

                    nameValuePairs.add(new BasicNameValuePair("bloodgroup", params[1]));

                }

                if (!bloodGroup.equals("Select Blood Group") && !city.equals("Select City")) {

                    nameValuePairs.add(new BasicNameValuePair("bloodgroup", params[1]));
                    nameValuePairs.add(new BasicNameValuePair("city", params[2]));
                }


                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();



                Log.d("TAG", "SSSSSSS " + jsonResult);






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

            bar.setVisibility(View.GONE);

            System.out.println("Resulted Value: " + result);

            if(result.equals("") || result == null){

                Toast.makeText(SearchForBlood.this, "Server connection failed",
                        Toast.LENGTH_LONG).show();

                return;

            }

            int jsonResult = returnParsedJsonObject(result);

            Log.e("TAG", "VVVVVVV" + result);
            URLRESULT = result;
            getData();



            if(jsonResult == 0){
               // getData();
                /*Toast.makeText(SearchForBlood.this, "Invalid username or password",
                        Toast.LENGTH_LONG).show();
*/
                return;

            }

            if(jsonResult == 1){




                //do all fatch result funtion here

                // getData();

            }

            myJSON=URLRESULT;
           // showList();
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

    protected void showList(){
        try {

            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);


            if (peoples.toString().equals("[]")){
                Log.e("TAG", "SSSSTT Emtpy");
                Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show();
            }

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String mobile_number = c.getString(TAG_MOBILE_NUMBER);
                String bloodgroup = c.getString(TAG_BLOODGROUP);
                String city = c.getString(TAG_CITY);
                String gender = c.getString(TAG_GENDER);
                String dob = c.getString(TAG_dob);

                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");

                Date theDate = format.parse(dob);

                Calendar myCal = new GregorianCalendar();
                myCal.setTime(theDate);

                Log.e("DATE", "RESULT: Daty " + myCal.get(Calendar.DAY_OF_MONTH));
                Log.e("DATE", "RESULT: Daty " + myCal.get(Calendar.MONTH) + 1);
                Log.e("DATE", "RESULT: Daty " + myCal.get(Calendar.YEAR));

                int providerYear = myCal.get(Calendar.YEAR);

                Calendar calendarCurrent = Calendar.getInstance();
                int currentYear = calendarCurrent.get(Calendar.YEAR);

                int age = currentYear-providerYear;
                String sAge = Integer.toString(age);
                Log.e("DATE", "RESULT: Daty " + age);



                HashMap<String,String> persons = new HashMap<String,String>();

                String mobile = mobile_number;
                mobile = mobile.substring(mobile.length()-3);
                mobile = "03******"+mobile;
                Log.e("TAG", "MMMMMMMMMM " + mobile );

                persons.put(TAG_NAME,name);
                persons.put(TAG_MOBILE_NUMBER,mobile_number);
                persons.put("TAG_MOB", mobile);
                persons.put(TAG_BLOODGROUP,bloodgroup);
                persons.put(TAG_CITY,city);
                persons.put(TAG_GENDER,gender);
                persons.put(TAG_dob,sAge);

                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    SearchForBlood.this, personList, R.layout.layout_for_searched_results,
                    new String[]{TAG_NAME,TAG_MOBILE_NUMBER, "TAG_MOB",TAG_BLOODGROUP, TAG_CITY,
                            TAG_GENDER, TAG_dob},
                    new int[]{R.id.searched_name,
                            R.id.searched_mobile_number,
                            R.id.searched_mobile,
                            R.id.searched_bloodgroup,
                            R.id.searched_city, R.id.searched_gender,
                            R.id.searched_dob}
            );

            resultListView.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost(serverUrl);

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader
                            (inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=URLRESULT;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_PHONE_CALL:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    call();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }

}
