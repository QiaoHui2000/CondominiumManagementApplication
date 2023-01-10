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
    ImageButton btnBack, btnEditFacility;
    Button btnAdd;
    private EditText etName, etLocation, etCapacity;
    private String facilityName,location,capacity;
    //String url1 = "http://10.131.77.213/";
    //String url1 = "http://192.168.1.14/";
    String url1 = "http://10.131.73.139/";
    //String url1 = "http://192.168.0.8/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_facility);

        //Get all Id's
        etName = (EditText) findViewById(R.id.editTxtFName);
        etLocation = (EditText) findViewById(R.id.editTxtFLocation);
        etCapacity = (EditText) findViewById(R.id.editTxtFCapacity);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnEditFacility = (ImageButton) findViewById(R.id.btnEditFacility);

        //Intent to Facilities Setting Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(AddNewFacility.this,
                        FacilitiesSettingMenu.class);
                startActivity(intentBack);
            }
        });

        //Intent to Edit Facility Info
        btnEditFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(AddNewFacility.this,
                        FacilitiesList.class);
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
                /*Intent intentSuccess = new Intent(AddNewFacility.this,
                        SuccessfulAddedFacility.class);
                startActivity(intentSuccess);*/
            }
    }

    //Insert data into database
    public void InsertData(final String facilityName, final String location, final String capacity) {
        // url to post our data
        String URL = url1+"insert_facility.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(AddNewFacility.this);

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    /*Toast.makeText(AddNewFacility.this,
                            jsonObject.getString("message"), Toast.LENGTH_SHORT).show();*/
                    if (jsonObject.get("code").equals("201")){
                        Intent intentSuccess = new Intent(AddNewFacility.this,
                                SuccessfulAddedFacility.class);
                        startActivity(intentSuccess);
                        Toast.makeText(AddNewFacility.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                    }
                    if (jsonObject.get("code").equals("202")){
                        Toast.makeText(AddNewFacility.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    if (jsonObject.get("code").equals("200")){
                        Toast.makeText(AddNewFacility.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

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

    }
}