package ftmk.workshop2.Condominium_Management_Application;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddNewFacility extends AppCompatActivity {

    //Declare EditText & Button
    ImageButton btnBack;
    Button btnAdd;
    private EditText etName, etLocation, etCapacity;
    private String facilityName,location,capacity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_facility);



        //Get all Id's
        etName = (EditText) findViewById(R.id.editTxtFName);
        etLocation = (EditText) findViewById(R.id.editTxtFLocation);
        etCapacity = (EditText) findViewById(R.id.editTxtFCapacity);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        //Intent to Facilities Setting Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(AddNewFacility.this,
                        FacilitiesSettingMenu.class);
                startActivity(intentBack);
            }
        });

        //Insert data into database once user click on button Add
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetFacility();
            }
        });

    }

    //Get the facility's details
    public  void GetFacility(){
        facilityName = etName.getText().toString().trim();
        location = etLocation.getText().toString().trim();
        capacity = etCapacity.getText().toString().trim();

            // Give a warning to user when the field is empty
            if(TextUtils.isEmpty(facilityName)){
                etName.setError("Please enter Facility Name");
            }else if (TextUtils.isEmpty(location)){
                etLocation.setError("Please enter Location");
            }else if (TextUtils.isEmpty(capacity)){
                etCapacity.setError("Please enter Capacity");
            }else {
                InsertData(facilityName, location, capacity);
                //Intent to Successful Added Facilities Screen
                Intent intentSuccess = new Intent(AddNewFacility.this,
                        SuccessfullAddedFacility.class);
                startActivity(intentSuccess);
            }
    }

    //Insert data into database
    public void InsertData(final String facilityName, final String location, final String capacity) {
        // url to post our data
        String URL = "http://192.168.1.9/insert_facility.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(AddNewFacility.this);

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    Toast.makeText(AddNewFacility.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                etName.setText("");
                etLocation.setText("");
                etCapacity.setText("");

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(AddNewFacility.this, "Fail to get response = " + error,
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("facilityName", facilityName);
                params.put("location", location);
                params.put("capacity", capacity);

                return params;
            }
        };

        queue.add(request);

        /*class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                String nameHolder = facilityName;
                String locationHolder = location;
                String capacityHolder = capacity;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("FacilityName", nameHolder));
                nameValuePairs.add(new BasicNameValuePair("Location", locationHolder));
                nameValuePairs.add(new BasicNameValuePair("Capacity", capacityHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(URL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();
                }catch (ClientProtocolException e){

                }catch (IOException e){

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Toast.makeText(AddNewFacility.this, "Data Submit Successfully",
                        Toast.LENGTH_LONG).show();

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(facilityName, location, capacity);

    }*/
    }
}