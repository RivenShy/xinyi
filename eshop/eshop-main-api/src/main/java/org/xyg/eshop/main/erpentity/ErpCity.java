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
@TableName("erp_cities")
@ApiModel(value = "ERP城市对象",description = "ERP城市对象")
public class ErpCity implements Serializable {

	@ApiModelProperty(value = "省代号")
	private String provinceCode;

	@ApiModelProperty(value = "城市代码")
	@TableId(type = IdType.NONE)
	private String cityCode;

	@ApiModelProperty(value = "城市名称")
	private String cityName;

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
		return PinYinUtils.getFirstLetter(cityName);
	}
}
