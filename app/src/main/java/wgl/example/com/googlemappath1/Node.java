package wgl.example.com.googlemappath1;

/**
 * Created by Jeong on 2016-09-22.
 */
public class Node {
    double wgsX_s, wgsY_s, wgsX_e, wgsY_e;
    int dis;
    public Node(double wgsX_s, double wgsY_s, double wgsX_e, double wgsY_e, int dis){
        this.wgsX_s=wgsX_s;
        this.wgsY_s=wgsY_s;
        this.wgsX_e=wgsX_e;
        this.wgsY_e=wgsY_e;
        this.dis=dis;
    }

    public void setWgsXS(double wgsX_s){
        this.wgsX_s=wgsX_s;
    }

    public void setWgsYS(double wgsY_s){
        this.wgsY_s=wgsY_s;
    }

    public void setWgsXE(double wgsX_e){
        this.wgsX_s=wgsX_s;
    }

    public void setWgsYE(double wgsY_e){
        this.wgsY_s=wgsY_s;
    }

    public void setDis(int dis){
        this.dis=dis;
    }

    public double getWgsXS(){
        return wgsX_s;
    }

    public double getWgsYS(){
        return wgsY_s;
    }

    public double getWgsXE(){
        return wgsX_e;
    }

    public double getWgsYE(){
        return wgsY_e;
    }

    public int getDis(){
        return dis;
    }
}
