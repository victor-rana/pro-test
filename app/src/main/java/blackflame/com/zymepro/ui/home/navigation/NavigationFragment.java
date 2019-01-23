package blackflame.com.zymepro.ui.home.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.ai.AlexaActivity;
import blackflame.com.zymepro.ui.ai.GoogleActivity;
import blackflame.com.zymepro.ui.profile.ActivityProfile;
import blackflame.com.zymepro.ui.shopping.ZymeShop;

/**
 * Created by Prashant on 29-03-2017.
 */

public class NavigationFragment extends CommonFragment {

    View containerView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    LinearLayout profile;
    ListView mlisListView;
    boolean mFromSavedInstanceState;
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    int mCurrentSelectedPosition = 0;
    private FragmentDrawerListener drawerListener;
    TextView textView_UserName,textView_Email;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNavigationDrawerItemTitles;
    DataModel[] drawerItem;
    Context mcontext;
    public NavigationFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }
    public void setUp(Context context, int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mcontext=context;


        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        CommonPreference.initializeInstance(getActivity());
        mlisListView = layout.findViewById(R.id.listview);
        profile= layout.findViewById(R.id.profile);
        textView_Email= layout.findViewById(R.id.email);
        textView_UserName= layout.findViewById(R.id.username);
        String email=CommonPreference.getInstance().getEmail();
        String name=CommonPreference.getInstance().getUserName();
        LinearLayout alexa=layout.findViewById(R.id.ll_alexa);
        
        LinearLayout home=layout.findViewById(R.id.ll_google_home);
        alexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), AlexaActivity.class);
                startActivity(i);
        
            }
        });
        RelativeLayout shop=layout.findViewById(R.id.rl_shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ZymeShop.class);
                startActivity(i);

            }
        });
        
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), GoogleActivity.class);
                startActivity(i);
    
            }
        });
        
        textView_Email.setText(email);
        textView_UserName.setText(name);
        drawerItem=new DataModel[12];
        drawerItem[0] = new DataModel(R.drawable.ic_realtime, "Home");
        drawerItem[1] = new DataModel(R.drawable.ic_alarm, "Past Notifications");
        drawerItem[2] = new DataModel(R.drawable.ic_document, "Document Wallet");
        drawerItem[3] = new DataModel(R.drawable.ic_geotag, "Manage Geotags");
        drawerItem[4] = new DataModel(R.drawable.ic_roadside, "Breakdown Assistance");
        drawerItem[5] = new DataModel(R.drawable.ic_video_cam, "Dashcam Videos");
        drawerItem[6] = new DataModel(R.drawable.ic_envelope, "Message From Team");
        drawerItem[7] = new DataModel(R.drawable.ic_promotion, "Refer & Earn");
        drawerItem[8]=new DataModel(R.drawable.ic_sent_mail,"Support");
        drawerItem[9]=new DataModel(R.drawable.ic_rate_us,"Rate us");
        drawerItem[10] = new DataModel(R.drawable.ic_about_us, "About us");
        drawerItem[11] = new DataModel(R.drawable.ic_logout, "Logout");

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(getContext(), R.layout.custom_drawer_item, drawerItem);
        mlisListView.setAdapter(adapter);

        mlisListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ActivityProfile.class);
                startActivity(i);
            }
        });

        return layout;
    }


    private void openActivity(Object activityToLaunch){
        Intent intent=new Intent(getActivity(),(Class<?>)activityToLaunch);
        startActivity(intent);

    }


    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mlisListView != null) {
            mlisListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(containerView);
        }
    }
    public void setDrawerListener(FragmentDrawerListener listener) {
        drawerListener = listener;
    }

    public interface FragmentDrawerListener {
    void onDrawerItemSelected(View view, int position);
    }

}
