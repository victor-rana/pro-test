package blackflame.com.zymepro.ui.dashcam.dashcamvideo.model;

import android.graphics.Bitmap;
import java.io.File;
import java.util.Date;

public class DashcamVideoModel {
  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  String length,time,date,size;

  public File getPath() {
    return path;
  }

  public void setPath(File path) {
    this.path = path;
  }

  File path;
  Bitmap bitmap;
  long file_length;

  public long getFile_duration_time() {
    return file_duration_time;
  }

  public void setFile_duration_time(long file_duration_time) {
    this.file_duration_time = file_duration_time;
  }

  long file_duration_time;
  Date date_file;

  public long getFile_length() {
    return file_length;
  }

  public void setFile_length(long file_length) {
    this.file_length = file_length;
  }

  public Date getDate_file() {
    return date_file;
  }

  public void setDate_file(Date date_file) {
    this.date_file = date_file;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
  }
}
