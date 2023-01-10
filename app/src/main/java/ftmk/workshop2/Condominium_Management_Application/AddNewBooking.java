package ftmk.workshop2.Condominium_Management_Application;

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

public class AddNewBooking extends AppCompatActivity {

    Spinner spinnerFacility, spinnerTime;
    ImageButton btnBack, btnEdit;
    EditText editBookingDate;
    Button btnBook;

    private String facilityName, bookingTime, bookingDate;
    private DatePickerDialog datePicker;

    MaintenanceAdapter maintenanceAdapter;
    public static ArrayList<Maintenance> maintenanceArrayList = new ArrayList<>();
    Maintenance maintenance;

    ArrayList<String> facilityList = new ArrayList<>();
    ArrayAdapter<String> facilityAdapter;
    RequestQueue requestQueue;
    //String url1 = "http://10.131.77.213/";
   // String url1 = "http://192.168.1.14/";
    String url1 = "http://10.131.73.139/";
    //String url1 = "http://192.168.0.8/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booking);

        //Get all Id's
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnEdit = (ImageButton) findViewById(R.id.btnEditBooking);
        editBookingDate = (EditText) findViewById(R.id.editBookingDate);

        editBookingDate.setOnTouchListener(new View.OnTouchListener() {
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
                    datePicker = new DatePickerDialog(AddNewBooking.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    editBookingDate.setText(year  + "-" + (monthOfYear + 1)
                                            + "-" + dayOfMonth);
                                }
                            }, year,month, day);
                    datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePicker.show();
                }
                return false;
            }
        });


        //Intent to Facilities Setting Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(AddNewBooking.this,
                        FacilityBookingMenu.class);
                startActivity(intentBack);
            }
        });

        //Intent to Booking List
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(AddNewBooking.this,
                        BookingList.class);
                startActivity(intentBack);
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        spinnerFacility = findViewById(R.id.spinnerFacility);
        String url = url1 + "populate_facility.php";
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
                        facilityAdapter = new ArrayAdapter<>(AddNewBooking.this,
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

        btnBook = (Button) findViewById(R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getBooking();
            }
        });

    }

    private void getBooking() {

        spinnerFacility = (Spinner) findViewById(R.id.spinnerFacility);
        spinnerTime = (Spinner) findViewById(R.id.spinnerTime);
        editBookingDate = (EditText) findViewById(R.id.editBookingDate);
        facilityName = spinnerFacility.getSelectedItem().toString().trim();
        bookingTime = spinnerTime.getSelectedItem().toString().trim();
        bookingDate = editBookingDate.getText().toString().trim();

        if(TextUtils.isEmpty(bookingDate)){
            editBookingDate.setError("Please select booking date.");
        }
        else {
            addBooking(facilityName,bookingTime,bookingDate);
            /*Intent intentBack = new Intent(AddNewBooking.this,
                    Successful_Booked_Facility.class);
            startActivity(intentBack);*/

        }
    }

    private void addBooking(String facilityName, String bookingTime, String bookingDate) {

        // url to post our data
        String url = url1+"insert_booking.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(AddNewBooking.this);

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
                    //Toast.makeText(AddNewBooking.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    if (jsonObject.get("code").equals("201")){
                        Intent intent = new Intent(AddNewBooking.this,Successful_Booked_Facility.class);
                        startActivity(intent);
                        Toast.makeText(AddNewBooking.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    if (jsonObject.get("code").equals("202")) {
                        Toast.makeText(AddNewBooking.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    if (jsonObject.get("code").equals("200")) {
                        Toast.makeText(AddNewBooking.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                        //btnBook.setEnabled(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(AddNewBooking.this, "Fail to get response = " + error,
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
                params.put("bookingTime", bookingTime);
                params.put("bookingDate", bookingDate);

                return params;
            }
        };
        queue.add(request);
    }

    /*public void getMaintenanceDate() {

        StringRequest request = new StringRequest(Request.Method.POST, url1 + "get_maintenance_date.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        maintenanceArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("item");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String MaintenanceID = object.getString("maintenanceID");
                                    String FacilityName = object.getString("facilityName");
                                    String MaintenanceTime = object.getString("maintenanceTime");
                                    String MaintenanceDate = object.getString("maintenanceDate");

                                    maintenance = new Maintenance(MaintenanceID, FacilityName, MaintenanceTime,MaintenanceDate);
                                    maintenanceArrayList.add(maintenance);
                                    maintenanceAdapter.notifyDataSetChanged();

                                    String date = object.getString("maintenanceDate");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddNewBooking.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }*/


}