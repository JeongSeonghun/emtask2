package wgl.example.com.emtask4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PhpDown task;
    SebcAdapter seAdapter;
    ArrayList<SebcH> al=new ArrayList<SebcH>();
    ListView seList;
    String total_num;
    TextView total_num_t;
    Button total_bt;
    Boolean all_ch=true; //모두보기 버튼 한번만 작동용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seList= (ListView)findViewById(R.id.list);
        total_num_t=(TextView)findViewById(R.id.tot_num);
        total_bt=(Button)findViewById(R.id.button);

        //json관련 초반 20개 표시, 총 개수 확인, asynctask 사용
        task = new PhpDown();
        task.execute("http://openapi.seoul.go.kr:8088/63766f77687368753535566668746d/json/SebcHistoricSiteKor/1/20/");

        //목록 클릭. intext활용 주소 및 명칭 보냄
        seList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent(getApplicationContext(),AddrActivity.class);
                intent.putExtra("name",al.get(i).getName());
                intent.putExtra("addr",al.get(i).getAddr());
                startActivity(intent);
            }
        });

        total_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(all_ch){
                    task= new PhpDown();
                    task.execute("http://openapi.seoul.go.kr:8088/63766f77687368753535566668746d/json/SebcHistoricSiteKor/21/"+
                            total_num);
                    all_ch=false;
                }

            }
        });
    }

    private class PhpDown extends AsyncTask<String, Integer,String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }

            } catch (Exception ex) {
                ex.printStackTrace();

            }

            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {

            String cate1, cate2, cate3, name, addr;
            //분류1, 분류2, 분류3, 명칭, 주소
            SebcH sebc_n;

            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja;
                //전체 숫자 저장 및 표시
                total_num= root.getJSONObject("SebcHistoricSiteKor").getString("list_total_count");
                total_num_t.setText("총 숫자: "+total_num);

                //유적지 관련 내용
                ja=root.getJSONObject("SebcHistoricSiteKor").getJSONArray("row");

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);

                    cate1= jo.getString("CATE1_NAME");
                    cate2= jo.getString("CATE2_NAME");
                    cate3= jo.getString("CATE3_NAME");
                    name= jo.getString("NAME_KOR");
                    //시,구,동 종합
                    addr= jo.getString("H_KOR_CITY")+" "+jo.getString("H_KOR_GU")+" "+jo.getString("H_KOR_DONG");

                    sebc_n=new SebcH(cate1, cate2, cate3, name, addr);
                    al.add(sebc_n);
                }

            } catch (JSONException e) {

                e.printStackTrace();

            }
            seAdapter= new SebcAdapter(getApplicationContext(), R.layout.sebc_list_item, al);
            seAdapter.notifyDataSetChanged();
            seList.setAdapter(seAdapter);
        }
    }
}
