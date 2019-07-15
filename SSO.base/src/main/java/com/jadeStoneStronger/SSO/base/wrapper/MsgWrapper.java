package com.jadeStoneStronger.SSO.base.wrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


/**
 * 接口返回通用信息包装器
 */
public class MsgWrapper {

	private static final Logger log = LoggerFactory.getLogger(MsgWrapper.class);

	private static final Map<Integer, String> errors = new HashMap<>();

	public static final int OK = 20000;
	
	static {
		try {
			Properties props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("error.properties"));
			for (Entry<Object, Object> ent : props.entrySet()) {
				String key = (String) ent.getKey();
				String value = (String) ent.getValue();
				errors.put(Integer.valueOf(key), value);
			}
		} catch (Exception e) {
			log.error("load error.properties exception",e);
		}
	}

	/**
	 * 返回成功
	 * @param t
	 * @return
	 */
	public static <T> RespMessage<T> success(final T t) {
		return new RespMessage<>(OK, t);
	}
	
	/**
	 * 返回成功,并增加返回list的总数
	 * @param t
	 * @param totalCount
	 * @return
	 */
	public static <T> RespMessage<T> success(final T t,String totalCount) {
		return new RespMessage<>(OK,null,t,totalCount);
	}

	/**
	 * 失败
	 * @param errorCode
	 * @return
	 */
	public static <T> RespMessage<T> error(int errorCode) {
		if (errorCode == OK) {
			return success(null);
		}
		String msg = errors.get(errorCode);
		return new RespMessage<>(errorCode, msg, null);
	}
	
	public static <T> RespMessage<T> error(String msg) {
		return new RespMessage<>(50000, msg, null);
	}
	
	/**
	 * 失败
	 * @param errorCode
	 * @return
	 */
	public static <T> RespMessage<T> errorWithPattern(int errorCode, Object patternStr) {
		if (errorCode == OK) {
			return success(null);
		}
		String msg = errors.get(errorCode);
		return new RespMessage<>(errorCode, String.format(msg, patternStr), null);
	}
	
	/**
	 * 失败
	 * @param errorCode
	 * @return
	 */
	public static <T> RespMessage<T> error(int errorCode,String errmsg) {
		if (errorCode == OK) {
			return success(null);
		}
		String msg = errors.get(errorCode);
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNoneBlank(msg)) {
			sb.append(msg)
			.append("\n")
			.append(errmsg);
		} else {
			sb.append(errmsg);
		}
		
		return new RespMessage<>(errorCode, sb.toString(), null);
	}
	
	/**
	 * 失败
	 * @param errorCode
	 * @param params
	 * @return
	 */
	public static <T> RespMessage<T> patternError(int errorCode,Object... params) {
		if (errorCode == OK) {
			return success(null);
		}
		String msg = errors.get(errorCode);
		return new RespMessage<>(errorCode, String.format(msg, params), null);
	}
	
	/**
	 * 失败
	 * @param errorCode
	 * @param e
	 * @return
	 */
	public static <T> RespMessage<T> error(int errorCode,Exception e) {
		if (errorCode == OK) {
			return success(null);
		}else if(errorCode == 50000){
			if(e != null ){
				StringBuilder sb = new StringBuilder();
				sb.append(e.getLocalizedMessage());
				sb.append("\n");
				return new RespMessage<>(errorCode, sb.toString(), null);
			}else{
				String msg = errors.get(errorCode);
				return new RespMessage<>(errorCode, msg, null);
			}
		}else{
			String msg = errors.get(errorCode);
			return new RespMessage<>(errorCode, msg, null);
		}
	}
}
