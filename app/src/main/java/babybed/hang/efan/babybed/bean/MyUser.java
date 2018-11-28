package babybed.hang.efan.babybed.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Efan on 2018/3/26.
 */
public class MyUser extends BmobUser {

    private String imgurl;
    private String userid;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
