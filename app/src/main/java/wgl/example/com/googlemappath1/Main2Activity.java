package wgl.example.com.googlemappath1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class Main2Activity extends AppCompatActivity {
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        list= (ListView)findViewById(R.id.list);

        Intent intent= getIntent();
        //Vector<Vector<LatLng>> test= new Vector();
        String temp= intent.getStringExtra("test");

        setList(temp);
    }

    public void setList(String temp){
        String pathCk_s;

        Vector<Vector<LatLng>> nodeVec= new Vector();

        try {
            if(temp.length()!=0){
                JSONObject gDirectJo = new JSONObject(temp);

                pathCk_s=gDirectJo.getString("status");

                DirectionsJSONParser parser = new DirectionsJSONParser();

                nodeVec=parser.parse(gDirectJo);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (temp.length() != 0) {
            Vector<LatLng> nodeAll= new Vector();
            for(int i=0; i<nodeVec.size(); i++){
                for(int j=0; j<nodeVec.get(i).size(); j++){
                    nodeAll.add(nodeVec.get(i).get(j));
                }
            }
            list.setAdapter(new NodeAdapter(getApplicationContext(), R.layout.info, nodeAll));
        }
    }
}
