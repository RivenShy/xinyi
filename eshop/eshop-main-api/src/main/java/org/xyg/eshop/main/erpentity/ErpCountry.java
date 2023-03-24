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

@Data
@TableName("erp_countries")
@ApiModel(value = "ERP国家对象", description = "ERP国家对象")
public class ErpCountry implements Serializable {

	@ApiModelProperty(value = "国家代码")
	@TableId(type = IdType.NONE)
	private String countryCode;

	@ApiModelProperty(value = "国家名称")
	private String countryName;

	@ApiModelProperty(value = "完整名称")
	private String description;

	@ApiModelProperty(value = "语言")
	private String language;

	@ApiModelProperty(value = "是否废弃")
	private String obsoleteFlag;

	@ApiModelProperty(value = "是否有子节点")
	@TableField(exist = false)
	private Boolean hasChildren;

	public String getLetter() {//移动端需要排序
		return PinYinUtils.getFirstLetter(countryName);
	}
}
