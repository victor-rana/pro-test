package blackflame.com.zymepro.ui.setting.mapstyle.model;

public class SelectableItem extends MapStyle{
  private boolean isSelected = false;


  public SelectableItem(MapStyle item,boolean isSelected) {
    super(item.getName(),item.getImage());
    this.isSelected = isSelected;
  }


  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }


}
