package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.DBEntity;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("eshop_product_model_lines")
@ApiModel(value = "易车产品型号维护表", description = "易车产品型号维护表")
public class ProductModelLines extends DBEntity implements Serializable {
	@ApiModelProperty(value = "车厂车型维护表Id")
	private Long headerId;
	@ApiModelProperty(value = "本厂型号")
	private String factoryMode;
	@ApiModelProperty(value = "产品名称")
	private String productName;
	@ApiModelProperty(value = "装车位置")
	private String loadingPosition;


	@ApiModelProperty(value = "产品种类/产品种类编码")
	private Integer productType;
	@ApiModelProperty(value = "加工种类/加工种类编码")
	private Integer machiningType;
	@ApiModelProperty(value = "印刷标识")
	private String printedLogo;
	@ApiModelProperty(value = "规格")
	private String specifications;
	@ApiModelProperty(value = "面积")
	private String acreage;
	@ApiModelProperty(value = "工艺")
	private String workmanship;
	@ApiModelProperty(value = "附件")
	private String attachment;
	@ApiModelProperty(value = "内外销")
	private Long type;
	@ApiModelProperty(value = "美国型号")
	private String usModels;
	@ApiModelProperty(value = "欧洲型号")
	private String europeanModels;
	@ApiModelProperty(value = "南非")
	private String southcAfrica;
	@ApiModelProperty(value = "澳洲")
	private String australia;
	@ApiModelProperty(value = "OEM型号")
	private String oemModel;

	/**
	 * xyg型号
	 */
	@ApiModelProperty(value = "xyg型号")
	private String xygType;
	/**
	 * 整车描述
	 */
	@ApiModelProperty(value = "整车描述")
	private String description;
	/**
	 * 英文描述
	 */
	@ApiModelProperty(value = "英文描述")
	private String descriptionEn;
	/**
	 * 库存组织id
	 */
	@ApiModelProperty(value = "库存组织id")
	private Long organizationId;
	/**
	 * 技术资料id
	 */
	@ApiModelProperty(value = "技术资料id")
	private Long technologyId;
	/**
	 * 物料编码
	 */
	@ApiModelProperty(value = "物料编码")
	private String materialCode;


	@ApiModelProperty(value = "中高对角")
	private String diagonal ;

	@ApiModelProperty(value = "标准箱数量")
	private String quantity ;

	@ApiModelProperty(value = "颜色")
	private String colour ;
	@ApiModelProperty(value = "备注")
	private String remark ;

	@ApiModelProperty(value = "木箱数量")
	private String packingQuantity1 ;
	@ApiModelProperty(value = "铁箱数量")
	private String packingQuantity2 ;

	@ApiModelProperty(value = "宽")
	private Long width ;

	@ApiModelProperty(value = "长")
	private Long length ;
	@ApiModelProperty(value = "特性")
	private String features;

	@ApiModelProperty(value = "单位")
	private String units;

	@ApiModelProperty(value = "物料id")
	private Long materialId;
	@ApiModelProperty(value = "装车位置编码")
	private String loadingPositionCode;
	@ApiModelProperty(value = "附件说明")
	private String attachmentExplanation;
	@ApiModelProperty(value = "原始产品型号")
	private String originalProductModel;

	@ApiModelProperty(value = "钨丝特性")
	private String trait ;

	@ApiModelProperty(value = "钨丝线价格")
	private BigDecimal wusiWirePrice ;

	@ApiModelProperty(value = "银浆线价格")
	private BigDecimal yjWirePrice;

	@ApiModelProperty(value = "黑边面积")
	private BigDecimal blackArea ;

	@ApiModelProperty(value = "孔数量")
	private String hole ;

	/**
	 * 启用生产/是否可生产 { Y/N}
	 */
	@ApiModelProperty(value = "启用生产/是否可生产 { Y/N}")
	private String wipFlag;
	@ApiModelProperty(value = "erp有效状态 Y 有效／　Ｎ　失效")
	private String enabledFlag;

	@ApiModelProperty(value = "毛重")
	private String grossWeight;
	@ApiModelProperty(value = "钨丝根数(条)")
	private String wsnum;

	@ApiModelProperty(value = "镜座")
	private String mirrorMount;
	@ApiModelProperty(value = "感应座R")
	private String inductionSeat;
	@ApiModelProperty(value = "胶条Y")
	private String adhesiveStrip;
	@ApiModelProperty(value = "线接头L/LH")
	private String lineConnector;
	@ApiModelProperty(value = "卡扣/易拉扣K/G/GK")
	private String snap;
	@ApiModelProperty(value = "塑胶件/五金W")
	private String hardware;
	@ApiModelProperty(value = "刹车灯支架J")
	private String brakeLightBracket;
	@ApiModelProperty(value = "孔位胶套O")
	private String holeRubberSleeve;

	@ApiModelProperty(value = "产品特性")
	private String productTrait;

	@ApiModelProperty(value = "客户颜色")
	private String partyColor ;

	@ApiModelProperty(value = "erp产品是否停用状态: Y 有效: N 失效")
	private String checkFlag;

	@ApiModelProperty(value = "模板id")
	private Long templateId;

	@ApiModelProperty(value = "产品类型(0:易车,1:自有)")
	private Integer itemType;

	@ApiModelProperty(value = "零售价")
	private BigDecimal retailPrice;

	@ApiModelProperty(value = "库存数量")
	private BigDecimal inventoryQuantity;

	@ApiModelProperty(value = "供应商")
	private String supplier;

	@ApiModelProperty(value = "产品图片")
	private String productImage;
}
