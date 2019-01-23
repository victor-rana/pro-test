package blackflame.com.zymepro.ui.setting.mapstyle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.setting.mapstyle.adapter.StyleAdapter;

import blackflame.com.zymepro.ui.setting.mapstyle.adapter.ViewHolder;
import blackflame.com.zymepro.ui.setting.mapstyle.model.MapStyle;
import blackflame.com.zymepro.ui.setting.mapstyle.model.SelectableItem;
import blackflame.com.zymepro.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;

public class MapStyleSetting extends BaseActivity implements ViewHolder.OnItemSelectedListener{
  RecyclerView recyclerView;
  StyleAdapter adapter;
  String[] titles;
  String[] images=new String[]{
      "https://getzyme.com/mapstyles/military_dark.png",
      "https://getzyme.com/mapstyles/exotic_original.png",
      "https://getzyme.com/mapstyles/nature_green.png",
      "https://getzyme.com/mapstyles/colorful_bliss.png",
      "https://getzyme.com/mapstyles/black_white.png",
      "https://getzyme.com/mapstyles/golden_hue.png",
      "https://getzyme.com/mapstyles/watery_blue.png",
      "https://getzyme.com/mapstyles/pink_hues.png",
      "https://getzyme.com/mapstyles/midnight_commander.png",
      "https://getzyme.com/mapstyles/sober_vintage.png",
  };
  ArrayList<MapStyle> list;
  String TAG=MapStyleSetting.class.getCanonicalName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map_style_setting);
    initViews();

  }

  private void initViews(){
    Toolbar toolbar= findViewById(R.id.toolbar_map_style);
    TextView textView_title= findViewById(R.id.toolbar_title_map);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,textView_title,"Map Styles");

    TextView tv_save=findViewById(R.id.toolbar_setting_map);
    tv_save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (adapter.lastCheckedPosition==-1){
          ToastUtils.showShort("Please select a style.");
        }else {
          MapStyle style=list.get(adapter.lastCheckedPosition);
          CommonPreference.getInstance().setSelectedPosition(adapter.lastCheckedPosition);
          CommonPreference.getInstance().setMapType(style.getName());
          Log.e(TAG, "onClick: "+CommonPreference.getInstance().getMapType() );
          ToastUtils.showShort("Saved");

        }
      }
    });

    recyclerView=findViewById(R.id.recycler_style);
    titles=getResources().getStringArray(R.array.style_name);


    list=getData(titles,images);
    adapter=new StyleAdapter(list,false);
    adapter.lastCheckedPosition=CommonPreference.getInstance().getSelectedPosition();
    recyclerView.setLayoutManager(new GridLayoutManager(MapStyleSetting.this,2));
    recyclerView.setAdapter(adapter);
    adapter.notifyDataSetChanged();
    RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
    itemAnimator.setAddDuration(1000);
    itemAnimator.setRemoveDuration(1000);
    recyclerView.setItemAnimator(itemAnimator);
  }
  private ArrayList<MapStyle> getData(String[] titles,String[] images){
    ArrayList<MapStyle> list=new ArrayList<>();
    for(int i=0;i<titles.length;i++){
      MapStyle style=new MapStyle(titles[i],images[i]);
      list.add(style);
    }
    return list;}

  @Override
  public void onItemSelected(SelectableItem item) {
    List<MapStyle> selectedItems = adapter.getSelectedItems();

  }
}
