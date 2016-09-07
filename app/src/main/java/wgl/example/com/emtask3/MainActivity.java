package wgl.example.com.emtask3;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView list;
    Spinner spinner;
    Button searchBt;
    EditText searchText;
    ArrayList<Inform> al= new ArrayList<Inform>();// 리스트뷰용
    ArrayAdapter adapter_sp; //스피너용
    String[] sp_list={"Key", "Value"}; //스피너 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list= (ListView)findViewById(R.id.infoList);
        spinner= (Spinner)findViewById(R.id.keyspin);
        searchBt= (Button)findViewById(R.id.s_bt);
        searchText= (EditText)findViewById(R.id.s_text);

        //arraylist al에 os, memory, cpu정보 추가
        AddSysInfo(al); //os 정보
        ReadMemInfo(al);    //memory 정보
        ReadCPUinfo(al);    //cpu 정보

        InfoAdapter adapter = new InfoAdapter(getApplicationContext(), R.layout.list_item, al);
        list.setAdapter(adapter);

        //스피너관련
        adapter_sp=new ArrayAdapter(this, android.R.layout.simple_spinner_item, sp_list);//환경, 스피너 항목 레이아웃, 리스트
        adapter_sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//펼처진 상태 레이아웃(배경)

        spinner.setAdapter(adapter_sp);

        /* 선택시 호출되는 메서드
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        */

        //search 버튼 클릭시, 어댑터 새로 설정(list 따로 저장 연결)
        searchBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String sel, sech;
                //spinner, editText의 값을 가져옴
                sel=spinner.getSelectedItem().toString();
                sech=searchText.getText().toString();
                ArrayList<Inform> s_al= new ArrayList<Inform>();

                //key 구분에 따른 list저장
                for(int i=0; i<al.size(); i++){
                    String str;
                    switch(sel){
                        case "Key":
                            str=al.get(i).getKey();
                            str=str.toLowerCase();  //대소문자 관계 없이 검색하기 위해서
                            //전체 리스트인 al에서 key값들 안에서 editText 검색 내용 포함을 확인 후 저장
                            if(str.contains(sech.toLowerCase()))
                                s_al.add(al.get(i));
                            break;
                        case "Value":
                            str=al.get(i).getValue();
                            if(str.contains(sech))
                                s_al.add(al.get(i));
                            break;
                    }
                }
                list.setAdapter(new InfoAdapter(getApplicationContext(), R.layout.list_item, s_al));
            }
        });

    }

    //시스템 정보 출력(os)
    //getProperty() 만든 함수
    //텍스트뷰에 출력할 text로 만듬

    //private String ReadSYSinfo() 변경
    private void AddSysInfo(ArrayList<Inform> al)
    {
        /*
        os.arch os아키텍처
        file.separator 파일구분자
        pathline.separator 경로구분자
        line.separator 형구분자
         */

        //Inform info= new Inform();
        String[] key={"OS Name", "OS Version", "JAVA Vendor URL", "JAVA Version", "JAVA Class Path",
                        "JAVA Class Version", "JAVA Vendor", "JAVA Install dir", "User Name",
                        "User Home dir", "Current dir"};
        String[] value={"os.name", "os.version", "java.vendor.url", "java.version", "java.class.patt",
                        "java.class.version", "java.vendor", "java.home", "user.name",
                        "user.home", "user.dir"};
        for(int i=0;i<key.length;i++){
            Inform info= new Inform();
            info.setInfo(key[i], getProInfo(value[i]));
            al.add(info);
        }
        //OS 이름, OS 버전, 자바 공급자 URL, 자바 버전, 자바 클래스 경로, 자바 클래스 버전, 자바 공급자
        //자바 설치 디렉토리, 사용자 계정, 사용자 홈 디렉토리, 현제 디렉토리

    }
    //System.getProperty() 사용
    //private void getProperty(String desc, String property, StringBuffer tBuffer) 변경
    //정보 명칭 : 값 문자열 형태로
    private String getProInfo(String property){
        String str;
        str=String.valueOf(System.getProperty(property));

        return str;
    }

    //memory 정보
    private String ReadMemInfo(ArrayList al)
    {
        ProcessBuilder cmd;

        StringBuffer strMemory = new StringBuffer();

        //ActivityManager 앱의 실행, 종료, 인탠트 전달/파라미터 시스템 내부의 상태 파악
        //getSystemService 시스템 레벨 서비스 제어관련/ Context클래스 메소드
        //ACTIVITY_SERVICE 파라미터 ActivityManager 이용시 사용
        ActivityManager actvityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo ();
        actvityManager.getMemoryInfo( mInfo );// memory정보 mInfo에 저장

        String result=strMemory.toString();


        Inform info= new Inform();
        info.setInfo("Available Memory", String.valueOf(mInfo.availMem));

        try{
            String[] args = {"/system/bin/cat", "/proc/meminfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();// 명령어 실행
            InputStream in = process.getInputStream();

            // 한 줄씩 읽어서 arraylist에 저장
            // BufferedReader, InputStreamReader 사용
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(in,"EUC_KR"));
            while(true){
                String string= bufferedReader.readLine();//한줄씩 읽어들임

                info= new Inform();
                if(string != null){
                    //":"기준으로 명칭과 값 나눔
                    String keystr, valstr;
                    int dnum;   //":" 구분할 위치
                    dnum=string.indexOf(":");
                    System.out.println("test: "+string);

                    if(!string.isEmpty()) { //빈 문자열 저장 방지
                        keystr= string.substring(0, dnum);
                        valstr= string.substring(dnum+1, string.length());
                        info.setInfo(keystr, valstr.trim());
                        al.add(info);
                    }
                }else{
                    break;
                }
            }

            in.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        return result;
    }

    /* 메모리 정보 구분
    안드로이드OS
    Available Memory : 남은 메모리

    MemTotal : 전체 메모리(예비 메모리 제외한)
    MemFree : 예비 메모리
    Buffers : 버퍼
    Cached : 캐시
    Dirty : 앱 정시에만 돌려 받는 메모리

     */


    //cpu 정보
    private String ReadCPUinfo(ArrayList al)
    {
        ProcessBuilder cmd;
        String result="";

        Inform info;

        try{
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();

            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(in,"EUC_KR"));

            while(true){
                String string= bufferedReader.readLine();

                info= new Inform();
                if(string != null){
                    //":"기준으로 명칭과 값 나눔
                    String keystr, valstr;
                    int dnum;   //":" 구분할 위치
                    dnum=string.indexOf(":");

                    if(!string.isEmpty()) {
                        keystr=string.substring(0, dnum - 1);
                        valstr=string.substring(dnum+1, string.length());
                        info.setInfo(keystr, valstr.trim());
                        al.add(info);
                    }
                }else{
                    break;
                }
            }
            in.close();

        } catch(IOException ex){
            ex.printStackTrace();
        }
        return result;
    }
}
