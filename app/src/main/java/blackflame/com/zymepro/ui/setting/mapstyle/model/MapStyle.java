package blackflame.com.zymepro.ui.setting.mapstyle.model;

public class MapStyle {
  String name;
  String image;

  public MapStyle(String name,String url){
    this.name=name;
    this.image=url;

  }
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
