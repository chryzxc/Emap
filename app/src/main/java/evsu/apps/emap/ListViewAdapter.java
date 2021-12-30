package evsu.apps.emap;

import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter{
    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Merge> Mergelist = null;
    private ArrayList<Merge> arraylist;
    public static Integer code = 0;
    public static String building,room,roomId,floor;


    public ListViewAdapter(Context context, List<Merge> Mergelist) {
        mContext = context;
        this.Mergelist = Mergelist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Merge>();
        this.arraylist.addAll(Mergelist);
    }

    public class ViewHolder {
        TextView room;
        TextView building;
        TextView roomId;

    }

    @Override
    public int getCount() {
        return Mergelist.size();
    }

    @Override
    public Merge getItem(int position) {
        return Mergelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.room = (TextView) view.findViewById(R.id.room);
            holder.building = (TextView) view.findViewById(R.id.building);
            holder.roomId = (TextView) view.findViewById(R.id.roomId);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.room.setText(Mergelist.get(position).getNewroom());
        holder.building.setText(Mergelist.get(position).getNewbuilding());
        holder.roomId.setText(Mergelist.get(position).getNewroomId());


        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                ListViewAdapter.building = Mergelist.get(position).getNewbuilding();
                ListViewAdapter.room = Mergelist.get(position).getNewroom();
                ListViewAdapter.roomId = Mergelist.get(position).getNewroomId();
                ListViewAdapter.floor = Mergelist.get(position).getNewfloor();
                code = 1;
                Search.popup = 2;
                Search.backSButton.performClick();


          /**      String bldg = Mergelist.get(position).getNewbuilding();
                String room = Mergelist.get(position).getNewroom();
                String roomid = Mergelist.get(position).getNewroomId();
                String floor = Mergelist.get(position).getNewfloor();
                Toast.makeText(mContext, "bldg:"+bldg +" room:"+ room +" roomid:"+ roomid+ " floor:"+floor, Toast.LENGTH_SHORT).show();
*/



            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Mergelist.clear();
        if (charText.length() == 0) {
            Mergelist.addAll(arraylist);
        }
        else
        {
            for (Merge wp : arraylist)
            {
                if (wp.getNewroom().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    Mergelist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}

