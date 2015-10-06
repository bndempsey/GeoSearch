package com.geosearch.dempsey.geosearch;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class QuestSelect extends ActionBarActivity {
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_select);
        listView = (ListView)findViewById(R.id.list);
        String [] places = new String[] {"Light","Stadium"}; //add more presets here, add coordinates to switch statement
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,places);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        startMap(view,43.005993,-75.984314);
                        break;
                    case 1:
                        startMap(view,32.6022,-85.4892);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Not a valid selection",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quest_select, menu);
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
    public void startMap(View view,double latVal,double lonVal){
        Intent intent = new Intent(this, MapActivity.class);
        Bundle b = new Bundle();
        b.putDouble("lat",latVal);
        b.putDouble("lon", lonVal);

        b.putInt("rad", 50);
        b.putInt("color", Color.argb(120,0,255,0));
        intent.putExtras(b);
        startActivity(intent);
    }

}
