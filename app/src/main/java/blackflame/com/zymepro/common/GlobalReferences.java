package blackflame.com.zymepro.common;

import android.location.Location;
import androidx.appcompat.widget.Toolbar;
import android.widget.LinearLayout;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.db.Pref;
import org.json.JSONObject;

public class GlobalReferences {
    private static GlobalReferences globalReferences =null;
    public static GlobalReferences getInstance(){
        if(globalReferences==null){
            return globalReferences = new GlobalReferences();
        }
        return globalReferences;
    }
    public BaseActivity baseActivity;
    public CommonFragment mCommonFragment;
    public LinearLayout progressBar;
    public Toolbar toolbar;
    public Pref pref;
    //public CheckForPermissions checkForPermissions;
    public JSONObject postParamUserProfile;
    public boolean isLocationAlertShown;
    public Location currentLocation;
}
