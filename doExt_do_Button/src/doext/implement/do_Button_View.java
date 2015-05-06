package doext.implement;

import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import core.DoServiceContainer;
import core.helper.DoIOHelper;
import core.helper.DoImageHandleHelper;
import core.helper.DoImageLoadHelper;
import core.helper.DoResourcesHelper;
import core.helper.DoTextHelper;
import core.helper.DoUIModuleHelper;
import core.helper.jsonparse.DoJsonNode;
import core.interfaces.DoIScriptEngine;
import core.interfaces.DoIUIModuleView;
import core.object.DoInvokeResult;
import core.object.DoUIModule;
import doext.define.do_Button_IMethod;
import doext.define.do_Button_MAbstract;

/**
 * 自定义扩展UIView组件实现类，此类必须继承相应VIEW类，并实现DoIUIModuleView,Do_Button_IMethod接口；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象； 获取DoInvokeResult对象方式new
 * DoInvokeResult(this.model.getUniqueKey());
 */
@SuppressLint("ClickableViewAccessibility")
public class do_Button_View extends Button implements DoIUIModuleView, do_Button_IMethod, OnTouchListener,OnClickListener {

	/**
	 * 每个UIview都会引用一个具体的model实例；
	 */
	private do_Button_MAbstract model;
	private float radius;

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public do_Button_View(Context context) {
		super(context);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		this.setPadding(1, 0, 1, 0);
	}

	private void onDrawBackgroundDrawable() {
		Bitmap bgBitmap = DoImageHandleHelper.drawableToBitmap(getBackground(), (int) this.model.getRealWidth(), (int) this.model.getRealHeight());
		Bitmap newBitmap = Bitmap.createBitmap(bgBitmap.getWidth(), bgBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawBitmap(bgBitmap, 0, 0, new Paint());
		BitmapDrawable bd = new BitmapDrawable(DoResourcesHelper.getResources(), createRadiusBitmap(newBitmap));
		setBackgroundDrawable(bd);
	}

	private Bitmap createRadiusBitmap(Bitmap bitmap) {
		if (this.radius > 0f) {
			return DoImageHandleHelper.createRoundBitmap(bitmap, getRadius());
		}
		return bitmap;
	}

	/**
	 * 初始化加载view准备,_doUIModule是对应当前UIView的model实例
	 */
	@Override
	public void loadView(DoUIModule _doUIModule) throws Exception {
		this.model = (do_Button_MAbstract) _doUIModule;
		/*
		 * ::setFocusable(false)不获取焦点，否则listview无法响应itemclick事件；
		 * 不影响自身点击，只是不能通过键盘获取焦点；或者由前端JS控制；
		 */
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX, DoUIModuleHelper.getDeviceFontSize(_doUIModule, "9"));
		this.setFocusable(false);
		this.setOnClickListener(this);
		this.setOnTouchListener(this);
	}

	/**
	 * 动态修改属性值时会被调用，方法返回值为true表示赋值有效，并执行onPropertiesChanged，否则不进行赋值；
	 * 
	 * @_changedValues<key,value>属性集（key名称、value值）；
	 */
	@Override
	public boolean onPropertiesChanging(Map<String, String> _changedValues) {
		return true;
	}

	/**
	 * 属性赋值成功后被调用，可以根据组件定义相关属性值修改UIView可视化操作；
	 * 
	 * @_changedValues<key,value>属性集（key名称、value值）；
	 */
	@Override
	public void onPropertiesChanged(Map<String, String> _changedValues) {
		DoUIModuleHelper.handleBasicViewProperChanged(this.model, _changedValues);
		DoUIModuleHelper.setFontProperty(this.model, _changedValues);
		if (_changedValues.containsKey("bgImage")) {
			try {
				setBackgroundDrawable(_changedValues);
			} catch (Exception _err) {
				DoServiceContainer.getLogEngine().writeError("DoButton setBgImage \n", _err);
			}
		}
		if (_changedValues.containsKey("radius")) {
			setRadius(DoTextHelper.strToFloat(_changedValues.get("radius"), 0f));
		}
	}

	private void setBackgroundDrawable(Map<String, String> _changedValues) throws Exception {
		String _bgImage = _changedValues.get("bgImage");
		if (_bgImage != null) {
			String _bgImageFillPath = DoIOHelper.getLocalFileFullPath(this.model.getCurrentPage().getCurrentApp(), _bgImage);
			Bitmap _bitmap = DoImageLoadHelper.getInstance().loadLocal(_bgImageFillPath,(int)this.model.getRealWidth(), (int)this.model.getRealHeight());
			BitmapDrawable _bitmapDrawable = null;
			if (_bitmap != null) {
				_bitmapDrawable = new BitmapDrawable(DoResourcesHelper.getResources(), _bitmap);
			}
			if (_bitmapDrawable != null) {
				setBackgroundDrawable(_bitmapDrawable);
			}
		}
	}

	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		// ...do something
		return false;
	}

	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用， 可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名 #如何执行异步方法回调？可以通过如下方法：
	 *                    _scriptEngine.callback(_callbackFuncName
	 *                    ,_invokeResult);
	 *                    参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 *                    获取DoInvokeResult对象方式new
	 *                    DoInvokeResult(this.model.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, DoJsonNode _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		// ...do something
		return false;
	}

	/**
	 * 释放资源处理，前端JS脚本调用closePage或执行removeui时会被调用；
	 */
	@Override
	public void onDispose() {
		// ...do something
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!this.model.getEventCenter().containsEvent("touch")){
			return false;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {	
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			doButtonView_TouchDown();
			break;
		case MotionEvent.ACTION_UP:
			doButtonView_TouchUp();
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		doButtonView_Touch();
	}

	private void doButtonView_Touch() {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.model.getUniqueKey());
		this.model.getEventCenter().fireEvent("touch", _invokeResult);
	}

	private void doButtonView_TouchUp() {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.model.getUniqueKey());
		this.model.getEventCenter().fireEvent("touchUp", _invokeResult);
	}

	private void doButtonView_TouchDown() {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.model.getUniqueKey());
		this.model.getEventCenter().fireEvent("touchDown", _invokeResult);
	}

	/**
	 * 重绘组件，构造组件时由系统框架自动调用；
	 * 或者由前端JS脚本调用组件onRedraw方法时被调用（注：通常是需要动态改变组件（X、Y、Width、Height）属性时手动调用）
	 */
	@Override
	public void onRedraw() {
		this.setLayoutParams(DoUIModuleHelper.getLayoutParams(this.model));
		onDrawBackgroundDrawable();
	}

	/**
	 * 获取当前model实例
	 */
	@Override
	public DoUIModule getModel() {
		return model;
	}
}