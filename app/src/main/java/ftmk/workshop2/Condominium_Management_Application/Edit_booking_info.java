package ftmk.workshop2.Condominium_Management_Application;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.android.volley.AuthFailureError;
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

public class Edit_booking_info extends AppCompatActivity {

    Spinner spinnerFacility, spinnerTime;
    ImageButton btnBack;
    EditText edTxtDate, etID;
    Button btnUpdate;

    private DatePickerDialog datePicker;
    ArrayList<String> facilityList = new ArrayList<>();
    ArrayAdapter<String> facilityAdapter;
    RequestQueue requestQueue;
    //String url1 = "http://10.131.77.213/";
    String url1 = "http://192.168.1.14/";
    //String url1 = "http://192.168.0.8/";

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking_info);

        //Get all Id's
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        spinnerFacility = (Spinner) findViewById(R.id.spinnerFacility);
        spinnerTime = (Spinner) findViewById(R.id.spinnerTime);
        edTxtDate = (EditText) findViewById(R.id.editTxtDate);
        etID = (EditText) findViewById(R.id.editTxtBID);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        edTxtDate.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //Initialize variable
                    final Calendar cldr = Calendar.getInstance();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = simpleDateFormat.format(cldr.getTime());

                    //Get day, month, year
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);

                    // date picker dialog
                    datePicker = new DatePickerDialog(Edit_booking_info.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    edTxtDate.setText(year + "-" + (monthOfYear + 1)
                                            + "-" + dayOfMonth);
                                }
                            }, year, month, day);
                    datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePicker.show();
                }
                return false;
            }
        });
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        etID.setText(BookingList.bookingArrayList.get(position).getBookingID());
        spinnerFacility.setPrompt(BookingList.bookingArrayList.get(position).getFacilityName());
        spinnerTime.setPrompt(BookingList.bookingArrayList.get(position).getBookingTime());
        edTxtDate.setText(BookingList.bookingArrayList.get(position).getBookingDate());

        //Intent to Booking List
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(Edit_booking_info.this,
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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String facilityName = jsonObject.optString("FacilityName");
                        facilityList.add(facilityName);
                        facilityAdapter = new ArrayAdapter<>(Edit_booking_info.this,
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
    }

    public void fnUpdateBooking(View view) {
        final String facilityName = spinnerFacility.getSelectedItem().toString().trim();
        final String bookingTime = spinnerTime.getSelectedItem().toString().trim();
        final String bookingDate = edTxtDate.getText().toString().trim();
        final String bookingID = etID.getText().toString().trim();

        // Give a warning to user when the field is empty
        if (TextUtils.isEmpty(facilityName)) {
            edTxtDate.setError("Please enter maintenance Date");
        } else {

            //Intent to Successful Added Maintenance Screen
            /*Intent intentSuccess = new Intent(Edit_booking_info.this,
                    Successful_Updated_Booking.class);
            startActivity(intentSuccess);*/
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating....");
        //progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url1+"update_booking.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(Edit_booking_info.this, response,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),BookingList.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Edit_booking_info.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("bookingID", bookingID);
                params.put("facilityName", facilityName);
                params.put("bookingTime", bookingTime);
                params.put("bookingDate", bookingDate);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Edit_booking_info.this);
        requestQueue.add(request);
    }
}