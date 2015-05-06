package doext.define;

import core.object.DoProperty;
import core.object.DoProperty.PropertyDataType;
import core.object.DoUIModule;


public abstract class do_Button_MAbstract extends DoUIModule{

	protected do_Button_MAbstract() throws Exception {
		super();
	}
	
	/**
	 * 初始化
	 */
	@Override
	public void onInit() throws Exception{
        super.onInit();
        //注册属性
		this.registProperty(new DoProperty("bgImage", PropertyDataType.String, "", false));
		this.registProperty(new DoProperty("fontColor", PropertyDataType.String, "000000FF", false));
		this.registProperty(new DoProperty("fontSize", PropertyDataType.Number, "9", false));
		this.registProperty(new DoProperty("fontStyle", PropertyDataType.String, "normal", false));
		this.registProperty(new DoProperty("radius", PropertyDataType.Number, "0", true));
		this.registProperty(new DoProperty("text", PropertyDataType.String, "", false));
	}
}