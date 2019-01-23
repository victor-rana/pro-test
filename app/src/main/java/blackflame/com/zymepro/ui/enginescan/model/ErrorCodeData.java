package blackflame.com.zymepro.ui.enginescan.model;

import java.io.Serializable;

/**
 * Created by Prashant on 03-05-2017.
 */

public class ErrorCodeData implements Serializable {
    String error;
    String title;
    String description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
