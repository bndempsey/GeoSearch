package com.geosearch.dempsey.geosearch;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.multiplayer.InvitationEntity;

import java.util.ArrayList;
import java.util.List;


public class CustomOptions extends ActionBarActivity {
    private Spinner colorSelect;
    private EditText latSet;
    private EditText lonSet;
    private double lat, lon;
    private SeekBar radSet;
    private TextView currentRadius;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_options);
        colorSelect = (Spinner) findViewById(R.id.spinner1);
        String [] colors = new String[]{"Red","Blue","Green","Orange"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,colors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSelect.setAdapter(adapter);
        latSet = (EditText)findViewById(R.id.editText);
        lonSet = (EditText)findViewById(R.id.editText2);
        radSet = (SeekBar)findViewById(R.id.seekBar);
        radSet.setProgress(50);
        radSet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentRadius.setText(String.valueOf(radSet.getProgress())+"m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        currentRadius = (TextView)findViewById(R.id.currentRadius);
        currentRadius.setText(radSet.getProgress() + "m");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startMap(View view){


        try {
            lat = Double.parseDouble(latSet.getText().toString());
            lon = Double.parseDouble(lonSet.getText().toString());
        } catch(final NumberFormatException e) {

        }

        if(checkValidity()&&Math.abs(lat)<=90&&Math.abs(lon)<=180) {
            Intent intent = new Intent(this, MapActivity.class);
            Bundle b = new Bundle();
            b.putDouble("lat",lat);
            b.putDouble("lon", lon);
            b.putInt("rad", radSet.getProgress());
            switch(colorSelect.getSelectedItemPosition()){
                case 0:
                    b.putInt("color", Color.argb(120, 255, 0, 0));
                    break;
                case 1:
                    b.putInt("color", Color.argb(120, 0, 255, 0));
                    break;
                case 2:
                    b.putInt("color", Color.argb(120, 0, 0, 255));
                    break;
                case 3:
                    b.putInt("color",Color.argb(120,255,140,0));
                    break;
                default:
                    Toast.makeText(this,"Abort Captain!",Toast.LENGTH_SHORT).show();
                    break;
            }
            intent.putExtras(b);

            startActivity(intent);
        }
        else{

            if(Math.abs(lat)>90) Toast.makeText(this, "Invalid Latitude Value",Toast.LENGTH_SHORT).show();
            if(Math.abs(lat)>90) Toast.makeText(this, "Invalid Longitude Value",Toast.LENGTH_SHORT).show();

        }
    }
    public boolean checkValidity() {
        if(TextUtils.isEmpty(latSet.getText().toString())){
            Toast.makeText(this, "Set a latitude value",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(lonSet.getText().toString())){
            Toast.makeText(this, "Set a longitude value",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
