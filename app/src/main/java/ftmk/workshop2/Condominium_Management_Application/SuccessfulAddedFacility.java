package ftmk.workshop2.Condominium_Management_Application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessfulAddedFacility extends AppCompatActivity {

    ImageButton btnBack, btnHome;
    Button btnViewFList, btnDone, btnMoreFacility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull_added_facility);

        //Get all Id's
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnViewFList = (Button) findViewById(R.id.btnViewFacilitiesList);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnMoreFacility = (Button) findViewById(R.id.btnAddMoreFacility);

        //Intent to Add Facility
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(SuccessfulAddedFacility.this,
                        AddNewFacility.class);
                startActivity(intentBack);
            }
        });

        //Intent to Facility Menu
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(SuccessfulAddedFacility.this,
                        FacilitiesSettingMenu.class);
                startActivity(intentBack);
            }
        });

        //Intent to Home Page
        /*btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(SuccessfulAddedFacility.this,
                        FacilitiesSettingMenu.class);
                startActivity(intentBack);
            }
        });*/

        //Intent to Facilities List
        btnViewFList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(SuccessfulAddedFacility.this,
                        FacilitiesList.class);
                startActivity(intentBack);
            }
        });

        //Intent to Add More Facility
        btnMoreFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(SuccessfulAddedFacility.this,
                        AddNewFacility.class);
                startActivity(intentBack);
            }
        });

    }
}