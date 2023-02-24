package org.xyg.eshop.main.erp.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springrabbit.core.tool.utils.CollectionUtil;
import org.springrabbit.core.tool.utils.DateUtil;
import org.springrabbit.core.tool.utils.StringUtil;
import org.springrabbit.system.cache.DictCache;
import org.springrabbit.system.entity.Dict;
import org.xyg.eshop.main.entity.ProductModelLines;

import java.util.Date;
import java.util.List;

@Data
@Slf4j
public class ErpModelLine {

	/**
	 * 整车编号/用于关联车厂车型
	 */
	@JsonProperty("WHOLEID_DISP")
	private String WHOLEID_DISP;

	/**
	 * 物料名称/本厂型号
	 */
	@JsonProperty("ITEM_DESC")
	private String ITEM_DESC;

	/**
	 * 产品名称
	 */
	@JsonProperty("CARNAME")
	private String CARNAME;

	/**
	 * 装车位置
	 */
	@JsonProperty("SEGMENT5")
	private String SEGMENT5;

	/**
	 * 产品种类
	 */
	@JsonProperty("SEGMENT3")
	private String SEGMENT3;

	/**
	 * 加工类型/加工种类
	 */
	@JsonProperty("SEGMENT4")
	private String SEGMENT4;

	/**
	 * 印刷面/印刷标识
	 */
	@JsonProperty("PRINTING_SURFACE")
	private String PRINTING_SURFACE;

	/**
	 * 产品规格/规格
	 */
	@JsonProperty("SPEC")
	private String SPEC;

	/**
	 * 单位面积(SQM)/面积
	 */
	@JsonProperty("PERAREA")
	private String PERAREA;

	/**
	 * 工艺类型/工艺
	 */
	@JsonProperty("TECHTYPE")
	private String TECHTYPE;

	/**
	 * 附件
	 */

	/**
	 * 美国代号
	 */
	@JsonProperty("USACODE")
	private String USACODE;

	/**
	 * 欧洲代号
	 */
	@JsonProperty("EURCODE")
	private String EURCODE;

	/**
	 * 南非代号
	 */
	@JsonProperty("SACODE")
	private String SACODE;

	/**
	 * 澳洲代号
	 */
	@JsonProperty("AUCODE")
	private String AUCODE;

	/**
	 * OEM编号
	 */
	@JsonProperty("OEMNO")
	private String OEMNO;

	/**
	 * 英文名称
	 */
	@JsonProperty("EN_DESC")
	private String EN_DESC;

	/**
	 * 库存组织ID
	 */
	@JsonProperty("ORGANIZATION_ID")
	private Long ORGANIZATION_ID;

	/**
	 * 客制化料号ID/技术资料id
	 */
	@JsonProperty("ITEM_ID")
	private Long ITEM_ID;

	/**
	 * 物料编码
	 */
	@JsonProperty("ITEM_NUMBER")
	private String ITEM_NUMBER;

	/**
	 * 中高对角
	 */
	@JsonProperty("BEVEL")
	private String BEVEL;

	/**
	 * 标准箱数量
	 */

	/**
	 * 颜色
	 */

	/**
	 * 高(MM)/宽
	 */
	@JsonProperty("KUAN")
	private String KUAN;

	/**
	 * 宽(MM)
	 */
	@JsonProperty("CHANG")
	private String CHANG;

	/**
	 * 木箱装箱数
	 */
	@JsonProperty("ZXSL")
	private String ZXSL;

	/**
	 * 产品型号
	 */
	@JsonProperty("CARSTYLE")
	private String CARSTYLE;

	/**
	 * 产品位置
	 */
	@JsonProperty("PLACE")
	private String PLACE;
	/**
	 * xyg型号 产品型号 和 产品位置的拼接组合体
	 */

	/**
	 * 相似车型说明
	 */
	@JsonProperty("WHATDIFFERENT")
	private String WHATDIFFERENT;

	/***
	 * 技术图纸编号
	 */
	@JsonProperty("DESIGN_NO")
	private String DESIGN_NO;

	/**
	 * 单位
	 */
	@JsonProperty("UOM")
	private String UOM;

	/**
	 * 物料id
	 */
	@JsonProperty("INVENTORY_ITEM_ID")
	private Long INVENTORY_ITEM_ID;

	/**
	 * 启用生产/是否可生产 { Y/N}
	 */
	@JsonProperty("WIP_FLAG")
	private String WIP_FLAG;
	@JsonProperty("CREATION_DATE")
	private Date CREATION_DATE;
	@JsonProperty("LAST_UPDATE_DATE")
	private Date LAST_UPDATE_DATE;

	/**
	 * 有效状态 Y 有效／　Ｎ　失效
	 */
	@JsonProperty("ENABLED_FLAG")
	private String ENABLED_FLAG;

	/**
	 * 毛重
	 */
	@JsonProperty("GROSS_WEIGHT")
	private String GROSS_WEIGHT;

	/**
	 * 孔数
	 * @return
	 */
	@JsonProperty("HOLE")
	private String HOLE;

	/**
	 * 钨丝根数(条)
	 * @return
	 */
	@JsonProperty("WSNUM")
	private String WSNUM;

	/**
	 * 镜座
	 * @return
	 */
	@JsonProperty("ATTRIBUTE1")
	private String ATTRIBUTE1;

	/**
	 * 感应座
	 */
	@JsonProperty("ATTRIBUTE2")
	private String ATTRIBUTE2;

	/**
	 * 胶条
	 */
	@JsonProperty("ATTRIBUTE3")
	private String ATTRIBUTE3;

	/**
	 * 线接头
	 */
	@JsonProperty("ATTRIBUTE4")
	private String ATTRIBUTE4;

	/**
	 * 卡扣/易拉扣
	 */
	@JsonProperty("ATTRIBUTE5")
	private String ATTRIBUTE5;

	/**
	 *  塑胶件/五金件
	 */
	@JsonProperty("ATTRIBUTE6")
	private String ATTRIBUTE6;

	/**
	 *  刹车灯支架
	 */
	@JsonProperty("ATTRIBUTE7")
	private String ATTRIBUTE7;

	/**
	 * 空位胶套
	 */
	@JsonProperty("ATTRIBUTE8")
	private String ATTRIBUTE8;

	/**
	 * 产品特性
	 */
	@JsonProperty("TRAIT")
	private String TRAIT;

	@JsonProperty("CHECK_FLAG")
	private String CHECK_FLAG;

	public ProductModelLines toDto() {
		Integer productType = null;
		List<Dict> productTypes = DictCache.getList("qbcrm_product_type");
		if (CollectionUtil.isNotEmpty(productTypes)) {
			if (StringUtil.isNotBlank(SEGMENT3)) {
				String value = productTypes.stream().filter(v -> v.getDictValue().equals(SEGMENT3)).map(Dict::getDictKey).findFirst().orElse(null);
				if (StringUtil.isNotBlank(value)) {
					productType = Integer.valueOf(value);
				}
			}
		}
		ProductModelLines modelLines = new ProductModelLines();
		modelLines.setFactoryMode(ITEM_DESC);
		modelLines.setProductName(CARNAME);
//		modelLines.setLoadingPosition(SEGMENT5);
		if (productType != null) {
			modelLines.setProductType(productType);
		}
		if (StringUtil.isNotBlank(SEGMENT4)) {
			try {
				modelLines.setMachiningType(Integer.valueOf(SEGMENT4));
			} catch (NumberFormatException e) {
				log.error("加工种类key值转换失败");
			}
		}
		modelLines.setPrintedLogo(PRINTING_SURFACE);
		modelLines.setSpecifications(SPEC);
		modelLines.setAcreage(PERAREA);
		modelLines.setWorkmanship(TECHTYPE);
		modelLines.setUsModels(USACODE);
		modelLines.setEuropeanModels(EURCODE);
		modelLines.setSouthcAfrica(SACODE);
		modelLines.setAustralia(AUCODE);
		modelLines.setOemModel(OEMNO);
		if (StringUtil.isNotBlank(CARSTYLE)) {
			if (StringUtil.isNotBlank(PLACE)) {
				modelLines.setXygType(String.format("%s %s", CARSTYLE, PLACE));
			} else {
				modelLines.setXygType(CARSTYLE);
			}
		}
		modelLines.setDescriptionEn(EN_DESC);
		modelLines.setOrganizationId(ORGANIZATION_ID);
		modelLines.setTechnologyId(ITEM_ID);
		modelLines.setMaterialCode(ITEM_NUMBER);
		modelLines.setDiagonal(BEVEL);
		modelLines.setPackingQuantity1(ZXSL);
		try {
			if (StringUtil.isNotBlank(KUAN)) {
				modelLines.setWidth(Long.valueOf(KUAN));
			}
		} catch (NumberFormatException e) {
			log.error("宽度数据转换失败");
		}
		try {
			if (StringUtil.isNotBlank(CHANG)) {
				modelLines.setLength(Long.valueOf(CHANG));
			}
		} catch (NumberFormatException e) {
			log.error("长度数据转换失败");
		}
		modelLines.setUnits(UOM);
		modelLines.setMaterialId(INVENTORY_ITEM_ID);
		modelLines.setWipFlag(WIP_FLAG);
		modelLines.setEnabledFlag(ENABLED_FLAG);
		modelLines.setCreationDate(CREATION_DATE == null ? DateUtil.now() : CREATION_DATE);
		modelLines.setLastUpdateDate(LAST_UPDATE_DATE == null ? DateUtil.now() : LAST_UPDATE_DATE);
		modelLines.setHole(HOLE);
		modelLines.setGrossWeight(GROSS_WEIGHT);
		modelLines.setWsnum(WSNUM);
		modelLines.setMirrorMount(ATTRIBUTE1);
		modelLines.setInductionSeat(ATTRIBUTE2);
		modelLines.setAdhesiveStrip(ATTRIBUTE3);
		modelLines.setLineConnector(ATTRIBUTE4);
		modelLines.setSnap(ATTRIBUTE5);
		modelLines.setHardware(ATTRIBUTE6);
		modelLines.setBrakeLightBracket(ATTRIBUTE7);
		modelLines.setHoleRubberSleeve(ATTRIBUTE8 );
		modelLines.setProductTrait(TRAIT);
		splitFactoryMode(modelLines);
		splicingAttachment(modelLines);
		modelLines.setCheckFlag(CHECK_FLAG);
		return modelLines;
	}

	private static void splitFactoryMode(ProductModelLines modelLine) {
		if (StringUtil.isNotBlank(modelLine.getFactoryMode())) {
			String value = null;
			try {
				value = modelLine.getFactoryMode().substring(modelLine.getFactoryMode().indexOf(" ")).trim();
			} catch (Exception e) {
				log.error("装车位置转换失败:", e);
			}
			if (StringUtil.isNotBlank(value) && !" ".equals(value)) {
				String[] split = value.split(",");
				modelLine.setLoadingPosition(split[0]);
				if (split.length > 3) {
					String colour = split[2];
					modelLine.setColour(colour);
				}
//				if (split.length >= 4) {
//					String attachment = split[3];
//					modelLine.setAttachment(attachment);
//				}
			}
		}
	}

	private static void splicingAttachment(ProductModelLines modelLine) {
		StringBuilder builder = new StringBuilder();
		if (StringUtil.isNotBlank(modelLine.getMirrorMount())) {
			builder.append(modelLine.getMirrorMount());
		}
		if (StringUtil.isNotBlank(modelLine.getInductionSeat())) {
			builder.append(modelLine.getInductionSeat());
		}
		if (StringUtil.isNotBlank(modelLine.getAdhesiveStrip())) {
			builder.append(modelLine.getAdhesiveStrip());
		}
		if (StringUtil.isNotBlank(modelLine.getSnap())) {
			builder.append(modelLine.getSnap());
		}
		if (StringUtil.isNotBlank(modelLine.getHardware())) {
			builder.append(modelLine.getHardware());
		}
		if (StringUtil.isNotBlank(builder.toString()) && !"null".equals(builder.toString())) {
			modelLine.setAttachment(builder.toString());
		} else {
			modelLine.setAttachment("N");
		}
	}
}
