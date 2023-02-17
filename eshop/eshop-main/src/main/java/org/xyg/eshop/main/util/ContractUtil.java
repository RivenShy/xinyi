package org.xyg.eshop.main.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContractUtil {

	/**
	 * 获取合同编号
	 * 合同序号生成规则为：年+月+日+顺序号（自动排序）2022+09+07+01
	 *
	 * @return
	 */
//	public String selectSerialNumber() {
//		String prefix = "YC";
//		int number = 1;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		String date = sdf.format(new Date()); // 格式化日期 date: 20210114
//		String count = contractMapper.selectSerialNumberMAX(date + "%");  //数据库查询查询最大合同序号后四位+1
//		if (count != null && count.trim() != "") {
//			number = number + Integer.parseInt(count);
//		}
//		DecimalFormat dft = new DecimalFormat("0000");
//		String code = dft.format(number); // 格式化为四位流水号 code: 0001
//		return date + code;
//	}

}
