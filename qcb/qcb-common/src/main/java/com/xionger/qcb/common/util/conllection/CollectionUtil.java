package com.xionger.qcb.common.util.conllection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xionger.qcb.common.util.date.DateUtil;



/**
 * 集合工具类
 * @author DWQ
 *
 */
public class CollectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionUtil.class);

	/**
	 * 判断集合是否为空
	 * @param coll
	 * @return
	 */
	public static boolean isEmpty(Collection<?> coll){
		return CollectionUtils.isEmpty(coll);
	}
	
	/**
	 * 判断集合是否不为空
	 * @param coll
	 * @return
	 */
	public static boolean isNotEmpty(Collection<?> coll){
		return CollectionUtils.isNotEmpty(coll);
	}
	
	/**
	 * 合并集合a b
	 * @param a
	 * @param b
	 * @return
	 */
	public static Collection<?> union(Collection<?> a,Collection<?> b){
		return CollectionUtils.union(a, b);
	}
	
	public static Object getFieldVal(Object obj,final Class clazz,final String sortName){
		Object o=null;
		try { 
			String methodName="get"+sortName.substring(0, 1).toUpperCase()+sortName.substring(1);
		    Field field=clazz.getDeclaredField(sortName);
            Method getMethod = clazz.getDeclaredMethod(methodName, field.getType().getClasses());//获得get方法  
            o=getMethod.invoke(obj);//执行get方法返回一个Object  
	    } catch (Exception e) {  
	        LOGGER.error("" + e);  
	    } 
		return o;
	}
	
	public static  List sortListForDate(List list,final Class clazz,final String sortName,final String dateStyle){
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				int ret = 0;
				Object a=getFieldVal(o1, clazz, sortName);
				Object b=getFieldVal(o2, clazz, sortName);
				Date d1=null;
				Date d2=null;
				if(a instanceof String){
					d1=DateUtil.stringToDate(a.toString(), dateStyle);
				}else{
					d1=(Date) a;
				}
				if(b instanceof String){
					d2=DateUtil.stringToDate(b.toString(), dateStyle);
				}else{
					d2=(Date) b;
				}
				if(null != d1 && d1.before(d2)) {
					ret = 1;
				}else {
					ret = -1;
				}
				return ret;
			}
		});
		return list;
	}
}
