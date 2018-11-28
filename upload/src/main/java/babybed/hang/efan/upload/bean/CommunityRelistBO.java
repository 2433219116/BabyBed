package babybed.hang.efan.upload.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Efan on 2018/3/20.
 */

public class CommunityRelistBO extends BmobObject {
    public String imgurl;
    public String tittle;
    public String category;
    public String date;
    public String number;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
