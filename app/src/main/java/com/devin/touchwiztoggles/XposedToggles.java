package com.devin.touchwiztoggles;

import static de.robv.android.xposed.XposedHelpers.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.*;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.*;
import java.io.*;
import org.apache.commons.lang3.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage.*;
import de.robv.android.xposed.callbacks.XC_LayoutInflated.*;

public class XposedToggles implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources
{

    private static String MODULE_PATH = null;
	private String[] defaultArray;
	private static ClassLoader classLoader;
	private Class<?> quickSettingPanel;
	public static Drawable background;
	public static Drawable overflowButton;
	
	private static String SYSTEM_UI = "com.android.systemui";

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
		// Make sure we have the file
		readFile();
    }
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals(SYSTEM_UI)) 
			return;
		classLoader = lpparam.classLoader;
	}
	

    @Override
    public void handleInitPackageResources(final InitPackageResourcesParam resparam) throws Throwable {
		
        if (!resparam.packageName.equals(SYSTEM_UI))
            return;
			
		quickSettingPanel = findClass("com.android.systemui.statusbar.policy.quicksetting.QuickSettingPanel", classLoader);
		
		XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
		defaultArray = resparam.res.getStringArray(resparam.res.getIdentifier("QuickSettingButtonAttribute", "array", SYSTEM_UI));
				String[] array = readFile();
		File file = new File("/data/data/com.devin.touchwiztoggles/files/error/");
		if (file.exists())
			return;
       
        resparam.res.setReplacement(
			SYSTEM_UI, "array", "QuickSettingButtonAttribute",
			array == null || array.length == 0 ? modRes.fwd(R.array.QuickSettingButtonAttribute) : array);
        if (array != null) for (String str : array) XposedBridge.log("TouchWizToggles - " + str);
		
    }
	
	
	

}
