package me.blue.rss;

/**
 * Created by blue on 2017/2/8.
 */

public class RssSource {
    private String name;
    private String address;
    private boolean isChosen;

    public RssSource(String name, String address,boolean isChosen){
        this.name=name;
        this.address=address;
        this.isChosen=isChosen;
    }

    public void setChosen(){
        this.isChosen=true;
    }
    public void setUnChosen()
    {
        this.isChosen=false;
    }

    public String getName(){
        return this.name;
    }

    public String getAddress(){
        return this.address;
    }

    public boolean IsChosen(){
        return this.isChosen;
    }
}