package org.xyg.eshop.main.excel.product;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductModelLinesExcel {

	@ExcelProperty("产品名称")
	private String productName;
	@ExcelProperty("本厂型号")
	private String factoryMode ;
	@ExcelProperty(value = "规格")
	private String specifications;
	@ExcelProperty("商品分类")
	private Long productType;
	@ExcelProperty("商品零售价")
	private BigDecimal retailPrice;
	@ExcelProperty(value = "商品库存单位")
	private String units;
	@ExcelProperty("商品库存数量")
	private BigDecimal inventoryQuantity;
	@ExcelProperty("商品供应商")
	private String supplier;
	@ExcelProperty("商品描述")
	private String description;
	/*@ExcelProperty("HEADER_ID")
	private Long headerId;
	@ApiModelProperty(value = "本厂型号")
	@ExcelProperty("FACTORY_MODE")
	private String factoryMode;
	@ApiModelProperty(value = "产品名称")
	@ExcelProperty("PRODUCT_NAME")
	private String productName;
	@ApiModelProperty(value = "装车位置")
	@ExcelProperty("LOADING_POSITION")
	private String loadingPosition;
	@ApiModelProperty(value = "年款")
	@ExcelProperty("MODEL_YEAR")
	private String modelYear;
	@ApiModelProperty(value = "玻璃图")
	@ExcelProperty("GLASS_DIAGRAM")
	private String glassDiagram;
	@ApiModelProperty(value = "大丝网")
	@ExcelProperty("BIG_SILK_NET")
	private String bigSilkNet;
	@ApiModelProperty(value = "外试图")
	@ExcelProperty("EXTERIOR_VIEW")
	private String exteriorView;
	@ApiModelProperty(value = "产品种类")
	@ExcelProperty("PRODUCT_TYPE")
	private Long productType;
	@ApiModelProperty(value = "加工种类")
	@ExcelProperty("MACHINING_TYPE")
	private Long machiningType;
	@ApiModelProperty(value = "印刷标识")
	@ExcelProperty("PRINTED_LOGO")
	private String printedLogo;
	@ApiModelProperty(value = "规格")
	@ExcelProperty("SPECIFICATIONS")
	private String specifications;
	@ApiModelProperty(value = "面积")
	@ExcelProperty("ACREAGE")
	private String acreage;
	@ApiModelProperty(value = "工艺")
	@ExcelProperty("WORKMANSHIP")
	private String workmanship;
	@ApiModelProperty(value = "附件")
	@ExcelProperty("ATTACHMENT")
	private String attachment;
	@ApiModelProperty(value = "内外销")
	@ExcelProperty("TYPE")
	private Long type;
	@ApiModelProperty(value = "美国型号")
	@ExcelProperty("US_MODELS")
	private String usModels;
	@ApiModelProperty(value = "欧洲型号")
	@ExcelProperty("EUROPEAN_MODELS")
	private String europeanModels;
	@ApiModelProperty(value = "南非")
	@ExcelProperty("SOUTHC_AFRICA")
	private String southcAfrica;
	@ApiModelProperty(value = "澳洲")
	@ExcelProperty("AUSTRALIA")
	private String australia;
	@ApiModelProperty(value = "OEM型号")
	@ExcelProperty("OEM_MODEL")
	private String oemModel;*/
}
