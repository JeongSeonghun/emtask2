package wgl.example.com.googlemappath1;

/**
 * Created by EMGRAM on 2016-09-21.
 */
public class Wgs84xy {
    double wgsX, wgsY;
    public Wgs84xy(double wgsX, double wgsY){
        this.wgsX=wgsX;
        this.wgsY=wgsY;
    }

    public Wgs84xy(){

    }

    public void setWgsX(double wgsX){
        this.wgsX=wgsX;
    }

    public void setWgsY(double wgsY){
        this.wgsY=wgsY;
    }

    public double getWgsX(){
        return wgsX;
    }

    public double getWgsY(){
        return wgsY;
    }
}
