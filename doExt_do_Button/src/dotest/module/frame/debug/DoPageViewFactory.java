package dotest.module.frame.debug;

import android.app.Activity;
import core.interfaces.DoIPageViewFactory;

public class DoPageViewFactory implements DoIPageViewFactory {
	
	private Activity currentActivity;
	
	@Override
	public void closePage(String _animationType, String _data, int _continue) {
		
	}

	@Override
	public Activity getAppContext() {
		// TODO Auto-generated method stub
		return currentActivity;
	}
	
	public void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}

	@Override
	public void openPage(String _pageID, String _appID, String _uiPath,
			String _scriptFile, String _animationType, String _data,
			String _isFullScreen, String _keyboardMode, String _callbackFuncName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closePage(String _animationType, String _data, String _id) {
		// TODO Auto-generated method stub
		
	}

}
