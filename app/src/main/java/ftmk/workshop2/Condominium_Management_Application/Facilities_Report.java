package ftmk.workshop2.Condominium_Management_Application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Facilities_Report extends AppCompatActivity {

    TextView txtReport;
    ImageButton btnBack,btnSearch;
    EditText edSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_report);

        txtReport = (TextView) findViewById(R.id.txtReport);
        /*btnSearch = (ImageButton) findViewById(R.id.btn_search);
        edSearch = (EditText) findViewById(R.id.editSearchFacility);*/


        //Intent to Booking List
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(Facilities_Report.this,
                        FacilitiesSettingMenu.class);
                startActivity(intentBack);
            }
        });

        /*btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {*/
                //String data = edSearch.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://10.131.73.139/booking_report.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        String bookingID = jsonObject.getString("BookingID");
                                        String facilityName = jsonObject.getString("FacilityName");
                                        String bookingTime = jsonObject.getString("BookingTime");
                                        String bookingDate = jsonObject.getString("BookingDate");

                                        txtReport.append("\n\nBooking ID: " + bookingID + "\nFacility Name: "
                                                + facilityName + "\nBooking Time: " + bookingTime + "\nBooking Date: "
                                                + bookingDate + "\n\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getLocalizedMessage());
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();

                        params.put("FacilityName", String.valueOf(edSearch));
                        return params;
                    }
                };
                queue.add(stringRequest);
            //}
        //});


    }


}