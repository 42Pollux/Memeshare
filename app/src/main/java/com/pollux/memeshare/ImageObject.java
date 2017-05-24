package com.pollux.memeshare;

import java.util.ArrayList;

/**
 * Created by pollux on 11.04.17.
 */

public class ImageObject {

    private String image_title;
    private String image_url;
    private ArrayList<String> tags;

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String android_version_name) {
        this.image_title = android_version_name;
    }

    public String getImage_Location() {
        return image_url;
    }

    public void setImage_Location(String android_image_url) {

        this.image_url = android_image_url;
    }

    public void setImageTags(ArrayList<String> _tags){
        tags = _tags;
    }

    public void addImageTag(String tag){
        tags.add(tag);
    }

    public void removeImageTag(String tag){
        tags.remove(tag);
    }

    public ArrayList<String> getImageTags(){
        return tags;
    }

}
