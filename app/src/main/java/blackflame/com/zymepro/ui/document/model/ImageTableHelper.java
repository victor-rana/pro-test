package blackflame.com.zymepro.ui.document.model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Prashant on 24-03-2017.
 */

public class ImageTableHelper implements Serializable {
    String name;
    String type;
    File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    //byte [] image;

    String last_modified,creation_date,path;


    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public ImageTableHelper(){}
    public ImageTableHelper(String name, String type, String path,String id,String creation_date,String last_modified){
        this.name=name;
        this.type=type;
        this.id=id;
        this.creation_date=creation_date;
        this.last_modified=last_modified;
        this.path=path;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public byte[] getImage() {
//        return image;
//    }
//
//    public void setImage(byte[] image) {
//        this.image = image;
//    }



}
