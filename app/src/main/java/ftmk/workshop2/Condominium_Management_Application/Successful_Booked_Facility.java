package ftmk.workshop2.Condominium_Management_Application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Successful_Booked_Facility extends AppCompatActivity {

    ImageButton btnBack, btnHome;
    Button btnViewBList, btnDone, btnAddMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_booked_facility);

        //Get all Id's
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnViewBList = (Button) findViewById(R.id.btnViewBookingList);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnAddMore = (Button) findViewById(R.id.btnAddMoreBooking);


        //Intent to Add Booking
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(Successful_Booked_Facility.this,
                        AddNewBooking.class);
                startActivity(intentBack);
            }
        });

        //Intent to Facility Setting Menu
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(Successful_Booked_Facility.this,
                        FacilitiesSettingMenu.class);
                startActivity(intentBack);
            }
        });

        //Intent to Booking List
        btnViewBList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(Successful_Booked_Facility.this,
                        BookingList.class);
                startActivity(intentBack);
            }
        });

        //Intent to Add Booking
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(Successful_Booked_Facility.this,
                        AddNewBooking.class);
                startActivity(intentBack);
            }
        });
    }
}