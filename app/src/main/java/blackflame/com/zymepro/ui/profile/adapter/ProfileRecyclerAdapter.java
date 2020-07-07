package blackflame.com.zymepro.ui.profile.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.profile.ActivityProfile;
import blackflame.com.zymepro.ui.profile.model.ProfileModel;
import java.util.ArrayList;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
  Context mContext;
  ArrayList<ProfileModel> arrayList_model;
  private static final int TYPE_FOOTER = 1;
  private static final int TYPE_ITEM = 2;
  ActivityProfile mProfile;
  public ProfileRecyclerAdapter(ActivityProfile profile,ArrayList<ProfileModel> profileList, Context context) {
    this.arrayList_model = profileList;
    this.mContext = context;
    this.mProfile=profile;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TYPE_ITEM) {
      //Inflating recycle view item layout
      View itemView = LayoutInflater
          .from(parent.getContext()).inflate(R.layout.layout_profile_card, parent, false);
      return new ItemViewHolder(itemView);
    }  else if (viewType == TYPE_FOOTER) {
      //Inflating footer view
      View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_add_card, parent, false);
      return new FooterViewHolder(itemView);
    } else return null;
  }
  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder,  int pos) {


    if(arrayList_model.size()>0) {
      if (holder instanceof FooterViewHolder) {
        FooterViewHolder footerHolder = (FooterViewHolder) holder;

        footerHolder.cardView_add_car.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mProfile.onAddcarCallback(holder.getAdapterPosition());
          }
        });

      } else if (holder instanceof ItemViewHolder) {
        final ProfileModel profileModel = arrayList_model.get(pos);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.textView_registration.setText(profileModel.getRegistration_number());
        String nickName = profileModel.getNickName();
        if (nickName!=null&&nickName.equals("null")) {
          nickName = "--";
        }
        itemViewHolder.textView_name.setText(nickName);
        itemViewHolder.progressBar_remain_days.setProgress(Integer.valueOf(profileModel.getSubscriptiondays()));
        // Log.e(TAG, "onBindViewHolder: "+Integer.valueOf(profileModel.getSubscriptiondays()) );
        final int days=Integer.valueOf(profileModel.getSubscriptiondays());
        if (days>0){
          itemViewHolder.textView_subscription_days.setText("Remaining - "+profileModel.getSubscriptiondays()+" days");

        }else{

          itemViewHolder.textView_subscription_days.setText("Expired");
          itemViewHolder.textView_subscription_days.setTextColor(Color.RED);

        }

        if (days>120){
          itemViewHolder.subscription.setClickable(false);
          itemViewHolder.subscription.setFocusable(false);
          itemViewHolder.subscription.setAlpha(0.5f);

        }
        itemViewHolder.subscription.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            final int days=Integer.valueOf(profileModel.getSubscriptiondays());
            mProfile.onSubscriptionCallback(holder.getAdapterPosition(),days);
          }
        });

        //itemViewHolder.textView_subscription_days.setText("Remaining - "+profileModel.getSubscriptiondays()+" days");
        itemViewHolder.textView_model.setText(profileModel.getBrand() + " " + profileModel.getModel());
        itemViewHolder.textView_cc.setText(profileModel.getEngineCc()+" CC");
        if (profileModel.getEngine_type().equals("DIESEL")) {
          itemViewHolder.textView_fuelType.setText("Diesel");
        } else {
          itemViewHolder.textView_fuelType.setText("Petrol");

        }

        // itemViewHolder.textView_car_status.setText(Character.toString(profileModel.getStatus().charAt(0)).toUpperCase()+profileModel.getStatus().substring(1).toLowerCase());
        itemViewHolder.textView_car_status.setText(capitalizeFirstLetter(profileModel.getStatus()));

        itemViewHolder.edit_car.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mProfile.onEditClickCallback(holder.getAdapterPosition());

          }
        });


      }
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position == arrayList_model.size()) {
      return TYPE_FOOTER;
    }
    return TYPE_ITEM;
  }





  @Override
  public int getItemCount() {
   // Log.e(TAG, "getItemCount: "+arrayList_model.size() );
    return arrayList_model.size()+1;
  }

  private class FooterViewHolder extends RecyclerView.ViewHolder {
    CardView cardView_add_car;

    public FooterViewHolder(View view) {
      super(view);
      cardView_add_car= view.findViewById(R.id.card_view2);

    }
  }

  private class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView textView_registration,
        textView_model,
        textView_status,textView_fuelType,
        textView_cc,textView_subscription_days,textView_car_status;
    ProgressBar progressBar_remain_days;
    TextView edit_car,subscription;

    ImageView imageView_status;
    TextView textView_name;
    CardView cardView_add_car,cardView_detail;
    public ItemViewHolder(View convertView) {
      super(convertView);
      textView_model= convertView.findViewById(R.id.profile_card_model);
      textView_name= convertView.findViewById(R.id.profile_card_name);
      textView_registration= convertView.findViewById(R.id.profile_card_registration);
//            textView_status= (TextView) convertView.findViewById(R.id.profile_card_status);
//            imageView_status= (ImageView) convertView.findViewById(R.id.iv_status);
      textView_fuelType= convertView.findViewById(R.id.profile_card_engine_type);
      textView_subscription_days= convertView.findViewById(R.id.tv_remaining_days);
      textView_car_status= convertView.findViewById(R.id.tv_car_status);
      progressBar_remain_days= convertView.findViewById(R.id.progress_bar);
      cardView_detail= convertView.findViewById(R.id.card_view1);
      textView_cc= convertView.findViewById(R.id.tv_engine_cc);
      edit_car= convertView.findViewById(R.id.tv_profile_edit_car_info);

      subscription= convertView.findViewById(R.id.tv_profile_subsription);

    }
  }
  public String capitalizeFirstLetter(String original) {
    if (original == null || original.length() == 0) {
      return original;
    }
    return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
  }
}
