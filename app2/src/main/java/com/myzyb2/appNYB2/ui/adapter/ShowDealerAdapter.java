package com.myzyb2.appNYB2.ui.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.myzyb2.appNYB2.R;
import com.myzyb2.appNYB2.javabean.Dealer;
import com.myzyb2.appNYB2.javabean.Dealer.ListDealer;
import com.myzyb2.appNYB2.ui.activity.my.DlearGetActivity;
import com.myzyb2.appNYB2.ui.activity.my.ShowDealerActivity;
import com.myzyb2.appNYB2.util.CommonDialog;
import com.myzyb2.appNYB2.util.CommonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by cuiz on 2016/2/29.
 */
public class ShowDealerAdapter extends BaseAdapter {
    private List<ListDealer> list;
    private Context context;

    public ShowDealerAdapter(Context context) {
        this.context = context;
    }

    public void setLIst(List list) {
        this.list = list;
    }

    @Override
    public int getCount() {

        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_show_dealer, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }


        viewHolder.tvAddress.setText(list.get(position).getAddress());
        viewHolder.tvGname.setText(list.get(position).getGname());
        viewHolder.tvUname.setText(list.get(position).getUname());
        Ion.with(context).load(list.get(position).getImg_url()).withBitmap()
                .placeholder(R.drawable.moren_photo)
                .error(R.drawable.moren_photo).intoImageView(viewHolder.ivHead);

        viewHolder.llAlreadyGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DlearGetActivity.class);
                intent.putExtra("gid", list.get(position).getId());
                context.startActivity(intent);

            }
        });
        viewHolder.llPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.get(position).getPhone()));
//                if (checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                    return;
//                }
                context.startActivity(intent);

            }
        });


        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_show_dealer.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_head)
        ImageView ivHead;
        @Bind(R.id.tv_gname)
        TextView tvGname;
        @Bind(R.id.tv_uname)
        TextView tvUname;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.ll_already_get)
        LinearLayout llAlreadyGet;
        @Bind(R.id.ll_phone)
        LinearLayout llPhone;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
