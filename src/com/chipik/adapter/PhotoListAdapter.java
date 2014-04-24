package com.chipik.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baasbox.android.BaasFile;
import com.chipik.BaseApplication;
import com.chipik.R;
import com.squareup.picasso.Picasso;

public class PhotoListAdapter extends BaseAdapter {
    //FIXME externalize this url
	String baasFileUrl = "http://192.168.1.4:9000/file/"; //local
    //String baasFileUrl = "http://baasbox-anilsharma.rhcloud.com/file/"; //remote

    private Context context;
    private List<BaasFile> photoList = new ArrayList<BaasFile>();
    
    private Picasso picasso = null;
    
	public PhotoListAdapter(Context context) {
        this.context = context;
        this.picasso = BaseApplication.getPicasso();
	}
	
	public void setPhotoList( List<BaasFile> photoList){
		this.photoList = photoList;
	}

	@Override
	public int getCount() {
		return photoList.size();
	}

	@Override
	public Object getItem(int position) {
		return photoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null){
        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	vi = inflater.inflate(R.layout.photo_row, null);
        }
        
        BaasFile baasFile = photoList.get(position);
        baasFile.getCreationDate();        
        TextView caption = (TextView) vi.findViewById(R.id.caption); // caption
        caption.setText(baasFile.getAttachedData().getString("caption","No Caption :("));
        
        TextView dateView = (TextView) vi.findViewById(R.id.dateView); // caption
        dateView.setText(baasFile.getCreationDate());
        
        ImageView list_image = (ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        picasso.load(baasFileUrl + baasFile.getId()).into(list_image);        
		return vi;
	}	
}
