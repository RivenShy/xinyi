package org.xyg.eshop.main.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.springframework.util.StringUtils;

public class PinYinUtils {

	/**
	 * 获取第一个字母
	 */
	public static String getFirstLetter(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		char ch = str.charAt(0);
		if (ch >= '\u4E00' && ch <= '\u9FFF') {
			String[] array = PinyinHelper.toHanyuPinyinStringArray(ch);
			if (array != null && array.length > 0) {
				return array[0].substring(0, 1);
			}
		}
		return str.substring(0, 1);
	}

	/**
	 * 获取所有字符的第一个字母
	 */
	public static String getFirstLetters(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		char[] chars = str.toCharArray();
		StringBuilder builder = new StringBuilder();
		for (char aChar : chars) {
			if (aChar < '\u4E00' || aChar > '\u9FFF') {
				builder.append(aChar);
				continue;
			}
			String[] array = PinyinHelper.toHanyuPinyinStringArray(aChar);
			if (array != null && array.length > 0) {
				builder.append(array[0].charAt(0));
			} else {
				builder.append(aChar);
			}
		}
		return builder.toString();
	}
}
