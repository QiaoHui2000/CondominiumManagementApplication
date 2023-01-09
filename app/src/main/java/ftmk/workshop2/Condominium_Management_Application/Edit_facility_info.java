package ftmk.workshop2.Condominium_Management_Application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Edit_facility_info extends AppCompatActivity {

    ImageButton btnBack;
    EditText etID, etName, etLocation, etCapacity;

    private int position;
    //String url1 = "http://10.131.77.213/";
    String url1 = "http://192.168.1.14/";
    //String url1 = "http://192.168.0.8/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_facility_info);

        //Get all Id's
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        etID = (EditText) findViewById(R.id.editTxtFID);
        etName = (EditText) findViewById(R.id.editTxtFName);
        etLocation = (EditText) findViewById(R.id.editTxtFLocation);
        etCapacity = (EditText) findViewById(R.id.editTxtFCapacity);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        etID.setText(FacilitiesList.facilityArrayList.get(position).getFacilityID());
        etName.setText(FacilitiesList.facilityArrayList.get(position).getFacilityName());
        etLocation.setText(FacilitiesList.facilityArrayList.get(position).getLocation());
        etCapacity.setText(FacilitiesList.facilityArrayList.get(position).getCapacity());


        //Intent to Facilities Setting Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(Edit_facility_info.this,
                        AddNewFacility.class);
                startActivity(intentBack);
            }
        });

    }


    public void fnUpdate(View view){
        final String facilityName = etName.getText().toString().trim();
        final String location = etLocation.getText().toString().trim();
        final String capacity = etCapacity.getText().toString().trim();
        final String facilityID = etID.getText().toString().trim();

        // Give a warning to user when the field is empty
        if(TextUtils.isEmpty(facilityName)){
            etName.setError("Please enter Facility Name");
        }else if (TextUtils.isEmpty(location)){
            etLocation.setError("Please enter Location");
        }else if (TextUtils.isEmpty(capacity)){
            etCapacity.setError("Please enter Capacity");
        }else {
            //UpdateData(facilityName, location, capacity);
            //Intent to Successful Added Facilities Screen
           /* Intent intentSuccess = new Intent(Edit_facility_info.this,
                    SuccessfulUpdatedFacility.class);
            startActivity(intentSuccess);*/
        }


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url1+"update_facility.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(Edit_facility_info.this, response,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),FacilitiesList.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Edit_facility_info.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("facilityID", facilityID);
                    params.put("facilityName", facilityName);
                    params.put("location", location);
                    params.put("capacity", capacity);

                    return params;
                }
            };
        RequestQueue requestQueue = Volley.newRequestQueue(Edit_facility_info.this);
        requestQueue.add(request);
        }
    }
