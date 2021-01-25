package com.example.spokenglovesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignsListAdapter extends BaseAdapter {
    ArrayList<SignsListContent> data;
    Context context;
    Integer index;
    AlertDialog.Builder builder;


    public SignsListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<SignsListContent>();
        data.add(new SignsListContent("Hungry",R.drawable.two));
        data.add(new SignsListContent("Sleep", R.drawable.two));
        data.add(new SignsListContent("Hungry",R.drawable.two));
        data.add(new SignsListContent("Sleep", R.drawable.two));
        data.add(new SignsListContent("Hungry",R.drawable.two));
        data.add(new SignsListContent("Sleep", R.drawable.two));
        data.add(new SignsListContent("Hungry",R.drawable.two));
        data.add(new SignsListContent("Sleep", R.drawable.two));
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.sign_list_item,parent,false);
        TextView tvName=row.findViewById(R.id.etName);
        ImageView img=row.findViewById(R.id.img);
        Button btnDelete=row.findViewById(R.id.btnDelete);
        btnDelete.setTag(position);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = (Integer) v.getTag();
                builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.dialog_message2)
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                data.remove(index.intValue());
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        final SignsListContent content=data.get(position);
        tvName.setText(content.signName);
        img.setImageResource(content.img);
        
        return row;
    }
}
