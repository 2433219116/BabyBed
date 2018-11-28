package babybed.hang.efan.babybed.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Efan on 2018/3/20.
 */

public class MusicRegridBO extends BmobObject{
    public String coverurl;
    public String name;
    public String number;

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
