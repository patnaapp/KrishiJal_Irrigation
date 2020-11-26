package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import bih.in.krishijal_irrigation.R;

public class Aahar_Sinchaai_YojyaActivity extends Activity {
    Spinner sp_panchayat,sp_village,spn_water_available;
    EditText edt_pipe_length,edt_distribution_pipe_inch,edt_distribution_pipe_lambai,edt_command_area,edt_yojna_lagat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aahar__sinchaai__yojya);

        initialization();
    }
    public  void initialization(){
        sp_panchayat=(Spinner)findViewById(R.id.sp_panchayat);
        sp_village=(Spinner)findViewById(R.id.sp_village);
        spn_water_available=(Spinner)findViewById(R.id.spn_water_available);

        edt_pipe_length=(EditText)findViewById(R.id.edt_pipe_length);
        edt_distribution_pipe_inch=(EditText)findViewById(R.id.edt_distribution_pipe_inch);
        edt_distribution_pipe_lambai=(EditText)findViewById(R.id.edt_distribution_pipe_lambai);
        edt_command_area=(EditText)findViewById(R.id.edt_command_area);
        edt_yojna_lagat=(EditText)findViewById(R.id.edt_yojna_lagat);
    }
}
