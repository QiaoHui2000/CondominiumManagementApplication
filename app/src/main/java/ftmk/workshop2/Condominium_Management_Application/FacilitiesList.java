package ftmk.workshop2.Condominium_Management_Application;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FacilitiesList extends AppCompatActivity {

    //Declare Button
    ImageButton btnBack;

    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    FacilityAdapter facilityAdapter;
    public static ArrayList<Facility> facilityArrayList = new ArrayList<>();
    //String url1 = "http://10.131.77.213/";
    //String url1 = "http://192.168.1.14/";
    String url1 = "http://10.131.73.139/";
    //String url1 = "http://192.168.0.8/";
    String url = url1+"get_facility.php";
    Facility facility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_list);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        listView = findViewById(R.id.facilityListView);
        facilityAdapter = new FacilityAdapter(this,facilityArrayList);
        listView.setAdapter(facilityAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long facilityID) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"View Data","Edit Data","Delete Data"};
                builder.setTitle(facilityArrayList.get(position).getFacilityName());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Dialog customDialog;
                        customDialog = new Dialog(FacilitiesList.this);
                        customDialog.setContentView(R.layout.custom_dialog);
                        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        customDialog.setCancelable(false);
                        customDialog.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;

                        Button delete = customDialog.findViewById(R.id.btn_delete);
                        Button cancel = customDialog.findViewById(R.id.btn_cancel);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customDialog.dismiss();
                            }
                        });
                        switch (i){

                            case 0:
                                startActivity(new Intent(getApplicationContext(),DetailFacility.class)
                                        .putExtra("position",position));
                                break;
                            case 1:
                                startActivity(new Intent(getApplicationContext(), Edit_facility_info.class)
                                        .putExtra("position",position));
                                break;
                            case 2:
                                customDialog.show();
                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        deleteData(facilityArrayList.get(position).getFacilityID());
                                        customDialog.dismiss();
                                        Intent intent = new Intent(FacilitiesList.this,
                                                FacilitiesList.class);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                    }
                                });
                                break;
                        }

                    }
                });

                builder.create().show();
            }
        });

        //Get all Id's
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intentBack = new Intent(FacilitiesList.this,
                        FacilitiesList.class);
                startActivity(intentBack);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });


        //Intent to Facilities Setting Menu
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(FacilitiesList.this,
                        FacilitiesSettingMenu.class);
                startActivity(intentBack);
            }
        });

        getData();


    }


    private void deleteData(final String facilityID) {

        StringRequest request = new StringRequest(Request.Method.POST, url1+"delete_facility.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Data Deleted")){
                            Toast.makeText(FacilitiesList.this, "Data Deleted Successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(FacilitiesList.this, "Data Not Deleted",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FacilitiesList.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("facilityID", facilityID);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    public void getData() {

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                facilityArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("item");

                    if(success.equals("1")){

                        for(int i=0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            String FacilityID = object.getString("facilityID");
                            String FacilityName = object.getString("facilityName");
                            String Location = object.getString("location");
                            String Capacity = object.getString("capacity");

                            facility = new Facility(FacilityID,FacilityName, Location, Capacity);
                            facilityArrayList.add(facility);
                            facilityAdapter.notifyDataSetChanged();
                        }
                    }

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FacilitiesList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


}


