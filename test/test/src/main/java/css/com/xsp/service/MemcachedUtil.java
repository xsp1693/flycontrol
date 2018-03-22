/**
 * <b>项目名：</b>test<br/>  
 * <b>包名：</b>css.com.xsp.service<br/>  
 * <b>文件名：</b>MemcachedUtil.java<br/>  
 * <b>版本信息：</b>1.0<br/>  
 * <b>日期：</b>2018年3月13日 上午10:20:00<br/>  
 * <b>COPYRIGHT 2010-2016 ALL RIGHTS RESERVED </b>-版权所有<br/>
 */
package css.com.xsp.service;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;

/**
 * @description TODO
 * @createTime 2018年3月13日 上午10:20:00
 * @modifyTime 
 * @author 
 * @version 1.0
 */
public class MemcachedUtil {
	private static MemcachedClient mcc=null;
	public static MemcachedClient getMcc(){
		try {
			if(mcc==null){
				mcc=new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
			}
			return mcc;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void disConnectMcc() {
		if(mcc!=null){
			mcc.shutdown();
		}
	}
	
}
