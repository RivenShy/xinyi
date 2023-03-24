package org.xyg.eshop.main.erpentity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.util.PinYinUtils;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("erp_areas")
@ApiModel(value = "ERP区/镇对象", description = "ERP区/镇对象")
public class ErpArea implements Serializable {

	@ApiModelProperty(value = "城市代码")
	private String cityCode;

	@ApiModelProperty(value = "区号")
	@TableId(type = IdType.NONE)
	private String areaCode;

	@ApiModelProperty(value = "地区名称")
	private String areaName;

	@ApiModelProperty(value = "启用标志")
	private String enableFlag;

	@ApiModelProperty(value = "语言,ZHS,US")
	private String language;

	@ApiModelProperty(value = "最后更新时间")
	private Date lastUpdateDate;

	public String getLetter() {//移动端需要排序
		return PinYinUtils.getFirstLetter(areaName);
	}
}
