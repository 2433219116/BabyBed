package babybed.hang.efan.babybed.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Efan on 2018/3/20.
 */

public class StoreTrolleyRelistBO extends BmobObject{
    public String  imgurl;
    public String  name;
    public String  price;
    public String  number;
    public Boolean isselect;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean getIsselect() {
        return isselect;
    }

    public void setIsselect(Boolean isselect) {
        this.isselect = isselect;
    }
}
