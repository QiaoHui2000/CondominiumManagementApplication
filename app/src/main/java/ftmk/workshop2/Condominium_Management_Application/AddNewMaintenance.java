package ftmk.workshop2.Condominium_Management_Application;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewMaintenance extends AppCompatActivity {

    Spinner spinnerFacility, spinnerTime, spinnerReason;
    ImageButton btnBack, btnEdit;
    EditText edTxtDate;
    Button btnSave;

    private String facilityName, maintenanceTime, maintenanceDate, maintenanceReason;
    private DatePickerDialog datePicker;

    //String url1 = "http://10.131.77.213/";
    //String url1 = "http://192.168.1.14/";
    String url1 = "http://10.131.73.139/";
    //String url1 = "http://192.168.0.8/";


    ArrayList<String> facilityList = new ArrayList<>();
    ArrayAdapter<String> facilityAdapter;
    RequestQueue requestQueue;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_maintenance);


        //Assign variable
        edTxtDate = (EditText) findViewById(R.id.editTxtDate);

        edTxtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    //Initialize variable
                    final Calendar cldr = Calendar.getInstance();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = simpleDateFormat.format(cldr.getTime());

                    //Get day, month, year
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);

                    // date picker dialog
                    datePicker = new DatePickerDialog(AddNewMaintenance.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    edTxtDate.setText(year  + "-" + (monthOfYear + 1)
                                            + "-" + dayOfMonth);
                                }
                            }, year,month, day);
                    datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePicker.show();
                }
                return false;
            }
        });

        //Get all Id's
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnEdit = (ImageButton) findViewById(R.id.btnEditMaintenance);

        //Intent to Facilities Setting Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(AddNewMaintenance.this,
                        FacilitiesSettingMenu.class);
                startActivity(intentBack);
            }
        });

        //Intent to Edit Maintenance
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(AddNewMaintenance.this,
                        MaintenanceList.class);
                startActivity(intentBack);
            }
        });



        requestQueue = Volley.newRequestQueue(this);
        spinnerFacility = findViewById(R.id.spinnerFacility);
        String url = url1+"populate_facility.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("facility");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String facilityName = jsonObject.optString("FacilityName");
                        facilityList.add(facilityName);
                        facilityAdapter = new ArrayAdapter<>(AddNewMaintenance.this,
                                android.R.layout.simple_spinner_item, facilityList);
                        facilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerFacility.setAdapter(facilityAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);


        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMaintenance();


            }
        });



    }

    private void getMaintenance() {

        spinnerFacility = (Spinner) findViewById(R.id.spinnerFacility);
        spinnerTime = (Spinner) findViewById(R.id.spinnerTime);
        spinnerReason = (Spinner) findViewById(R.id.spinnerReason);
        edTxtDate = (EditText) findViewById(R.id.editTxtDate);
        facilityName = spinnerFacility.getSelectedItem().toString().trim();
        maintenanceTime = spinnerTime.getSelectedItem().toString().trim();
        maintenanceReason = spinnerReason.getSelectedItem().toString().trim();
        maintenanceDate = edTxtDate.getText().toString().trim();

        if(TextUtils.isEmpty(maintenanceDate)){
            edTxtDate.setError("Please select maintenance date.");
        }
        else {
            addMaintenance(facilityName,maintenanceTime,maintenanceDate, maintenanceReason);
            Intent intentBack = new Intent(AddNewMaintenance.this,
                    Successful_Saved_Maintenance.class);
            startActivity(intentBack);

        }

    }

    private void addMaintenance(String facilityName, String maintenanceTime, String maintenanceDate, String maintenanceReason)
    {
        // url to post our data
        String url = url1+"insert_maintenance.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(AddNewMaintenance.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    Toast.makeText(AddNewMaintenance.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(AddNewMaintenance.this, "Fail to get response = " + error,
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("facilityName", facilityName);
                params.put("maintenanceTime", maintenanceTime);
                params.put("maintenanceDate", maintenanceDate);
                params.put("maintenanceReason", maintenanceReason);

                return params;
            }
        };
        queue.add(request);
    }

}