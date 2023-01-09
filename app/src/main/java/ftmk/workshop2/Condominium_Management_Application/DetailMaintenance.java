package ftmk.workshop2.Condominium_Management_Application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailMaintenance extends AppCompatActivity {

    TextView tvid,tvName,tvTime,tvDate,tvReason;
    ImageButton btnBack;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_maintenance);

        //Initializing Views
        tvid = findViewById(R.id.txtid);
        tvName = findViewById(R.id.txtName);
        tvTime = findViewById(R.id.txtTime);
        tvDate = findViewById(R.id.txtDate);
        tvReason = findViewById(R.id.txtReason);
        btnBack = (ImageButton)findViewById(R.id.btnBack);

        //Intent to Facilities List
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(DetailMaintenance.this,
                        MaintenanceList.class);
                startActivity(intentBack);
            }
        });

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        tvid.setText("ID: "+MaintenanceList.maintenanceArrayList.get(position).getMaintenanceID());
        tvName.setText("Facility Name: "+MaintenanceList.maintenanceArrayList.get(position).getFacilityName());
        tvTime.setText("Maintenance Time: "+MaintenanceList.maintenanceArrayList.get(position).getMaintenanceTime());
        tvDate.setText("Maintenance Date: "+MaintenanceList.maintenanceArrayList.get(position).getMaintenanceDate());
        tvReason.setText("Maintenance Reason: "+MaintenanceList.maintenanceArrayList.get(position).getMaintenanceReason());

    }
}
