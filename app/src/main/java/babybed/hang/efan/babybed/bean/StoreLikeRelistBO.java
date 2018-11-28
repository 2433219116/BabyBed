package babybed.hang.efan.babybed.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Efan on 2018/3/17.
 */

public class StoreLikeRelistBO extends BmobObject {
    public String  imgurl;
    public String  name;
    public String  price;
    public boolean collect;
    public boolean buy;

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

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }
}
