package com.chipik.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.baasbox.android.BaasFile;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.chipik.R;
import com.chipik.adapter.PhotoListAdapter;

/**
 * Show photos uploaded by user.
 * @author anil sharma
 *
 */
public class PikStreamFragment extends Fragment {
	public static final String TAG = "PicDiary/PhotosFragment";
	
	ListView photolistView;
	PhotoListAdapter adapter;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pikstream, container, false);
        photolistView = (ListView)rootView.findViewById(R.id.photolistView);
        photolistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				Toast.makeText(getActivity(), "Item " + position + " clicked", Toast.LENGTH_SHORT).show();				
			}
        	
		});
        refresh();
        return rootView;
    }
	
	public void refresh(){
		BaasFile.fetchAll(handler);
	}
	
	final BaasHandler<List<BaasFile>> handler = new BaasHandler<List<BaasFile>>() {
		@Override
		public void handle(BaasResult<List<BaasFile>> result) {
			if( result.isSuccess() ) {
		        adapter = new PhotoListAdapter(getActivity());
				photolistView.setAdapter(adapter);
				adapter.setPhotoList(result.value());
				adapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "Photo fetching successful", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Photo fetching failed " + result.error().getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
}