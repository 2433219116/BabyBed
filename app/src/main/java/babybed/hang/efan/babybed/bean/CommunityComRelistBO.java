package babybed.hang.efan.babybed.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Efan on 2018/4/1.
 */

public class CommunityComRelistBO extends BmobObject {
    public String imgurl;
    public String name;
    public String date;
    public String content;
    public String position;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
