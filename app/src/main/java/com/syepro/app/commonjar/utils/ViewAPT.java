/**  

 * @Title: ViewAPT.java 

 * @Description: annotation processing tools

 * @author seek 951882080@qq.com  

 * @version V1.0  

 */

package com.syepro.app.commonjar.utils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 提取我们自定义的Annotation的工具类
 *
 */
public class ViewAPT {
	
	//点击事件名字，你可以自定义你的名�?
	private final static String CLICK_METHOD_NAME = "onClick";

	private ViewAPT() {
	}
	
	public static void init(Activity context) {
		init(context, context.getWindow().getDecorView());
	}
	
	/**
	 * 初始化方法，调用该工具类的这个方法，可以反射获取我们定义的Annotation信息，并执行view和id的绑定已经点击事�?
	 * @param context
	 * @param parent
	 */
	public static void init(Object context, View parent) {
		Field[] fields = context.getClass().getDeclaredFields();
		if (fields == null || fields.length <= 0)
			return;
		for (Field f : fields) {
			try {
				f.setAccessible(true);
				if (f.get(context) != null)
					continue;
				ViewInjection injection = f.getAnnotation(ViewInjection.class);
				if (injection != null) {
					int viewId = injection.value();
					f.set(context, parent.findViewById(viewId));
					setListener(context, f);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 设置点击事件
	 * @param context
	 * @param f
	 */
	private static void setListener(final Object context, Field f) {
		try {
			final Object view = f.get(context);
			if (view instanceof View) {
				((View) view).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Method m = context.getClass().getMethod(
									CLICK_METHOD_NAME, View.class);
							m.setAccessible(true);
							m.invoke(context, view);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}