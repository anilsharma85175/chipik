package com.chipik.fragment;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.support.v4.app.FragmentActivity;

import com.commonsware.cwac.camera.CameraUtils;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;

public class CustomCameraHost extends SimpleCameraHost {
	OnCameraClickedCallBack mCallback;
    public interface OnCameraClickedCallBack {
        public void onPhotoClicked();
    }
    
	public CustomCameraHost(FragmentActivity activity) {
	      super(activity);
	      try {
	            mCallback = (OnCameraClickedCallBack) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement OnCameraClickedListener");
	        }	      
	}
	
	@Override
	public Size getPreviewSize(int displayOrientation, int width, int height,
			Parameters parameters) {
		// TODO Auto-generated method stub
		return super.getPreviewSize(displayOrientation, width, height, parameters);
	}
	
	@Override
	public Size getPictureSize(PictureTransaction xact, Parameters parameters) {
		Camera.Size result=null;
		for (Camera.Size size : parameters.getSupportedPictureSizes()) {
			if (size.width == size.height){
				result = size;
				break;
			}
		}
		if (result != null) return result;
		return CameraUtils.getSmallestPictureSize(parameters);
	}
	
	@Override
	public boolean useSingleShotMode() {
		return true;
	}

	@Override
    public void saveImage(PictureTransaction xact, byte[] image) {
		if (useSingleShotMode()) {
			PostFragment.imageData = image;
			mCallback.onPhotoClicked();
		}else{
			super.saveImage(xact, image);
		}
    }

}
