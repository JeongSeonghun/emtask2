package wgl.example.com.googlemappath1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class ListActivity extends AppCompatActivity {
    ListView list;
    String receive="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        list= (ListView)findViewById(R.id.list);

        Intent intent= getIntent();

        receive= intent.getStringExtra("list");

        setList(receive);
    }

    //경로 node들 리스트뷰에 표시
    public void setList(String receive){

        Vector<Vector<LatLng>> nodeVec= new Vector();
        String pathCk_s="";

        try {


            JSONObject gDirectJo = new JSONObject(receive);

            pathCk_s=gDirectJo.getString("status");

            DirectionsJSONParser parser = new DirectionsJSONParser();

            nodeVec=parser.parse(gDirectJo);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(pathCk_s.equals("OK")){
            Vector<LatLng> nodeAll= new Vector();
            for(int i=0; i<nodeVec.size(); i++){        //i번째 steps
                for(int j=0; j<nodeVec.get(i).size(); j++){ //steps배열의 j번째 LatLng 객체
                    nodeAll.add(nodeVec.get(i).get(j));
                }
                list.setAdapter(new NodeAdapter(getApplicationContext(), R.layout.info, nodeAll));
            }
        }

    }
}
