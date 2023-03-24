package org.xyg.eshop.main.erpentity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.util.PinYinUtils;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("erp_provinces")
@ApiModel(value = "ERP省/州对象", description = "ERP省/州对象")
public class ErpProvince implements Serializable {

	@ApiModelProperty(value = "国家代码")
	private String countryCode;

	@ApiModelProperty(value = "省代号")
	@TableId(type = IdType.NONE)
	private String provinceCode;

	@ApiModelProperty(value = "省名")
	private String provinceName;

	@ApiModelProperty(value = "启用标志")
	private String enableFlag;

	@ApiModelProperty(value = "语言,ZHS,US")
	private String language;

	@ApiModelProperty(value = "最后更新时间")
	private Date lastUpdateDate;

	@ApiModelProperty(value = "是否有子节点")
	@TableField(exist = false)
	private Boolean hasChildren;

	public String getLetter() {//移动端需要排序
		if ("重庆市".equals(provinceName)) {
			return "c";
		}
		return PinYinUtils.getFirstLetter(provinceName);
	}
}
