package org.xyg.eshop.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springrabbit.core.mp.base.BaseEntity;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "ESHOP_MEMBER_MEMBER")
@Data
@ApiModel(value = "会员信息", description = "会员信息")
public class Member extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "会员名称")
	@TableField(value = "MEMBER_NAME")
	private String memberName;

	@ApiModelProperty(value = "联系方式")
	@TableField(value = "CONTACT_WAY")
	private String contactWay;

	@ApiModelProperty(value = "会员等级")
	@TableField(value = "MEMBER_GRADE")
	private String memberGrade;

	@ApiModelProperty(value = "会员号")
	@TableField(value = "MEMBER_CODE")
	private String memberCode;

	@ApiModelProperty(value = "邮箱")
	@TableField(value = "MAILBOX")
	private String mailbox;

	@ApiModelProperty(value = "微信号")
	@TableField(value = "WECHAT_NUMBER")
	private String wechatNumber;

	@ApiModelProperty(value = "所属门店ID")
	@TableField(value = "STORE_FRONT_ID")
	private Long storefrontId;

	@ApiModelProperty(value = "会员地址")
	@TableField(value = "MEMBER_ADDRESS")
	private String memberAddress;

	@ApiModelProperty(value = "会员车牌号")
	@TableField(value = "PLATE_NUMBER")
	private String plateNumber;

	@ApiModelProperty(value = "会员车型")
	@TableField(value = "CAR_MODEL")
	private String carModel;

	@ApiModelProperty(value = "会员车型标准")
	@TableField(value = "CAR_MODEL_STANDARD")
	private String carModelStandard;

	@ApiModelProperty(value = "会员有效期")
	@TableField(value = "TERM_OF_VALIDITY")
	private Integer termOfValidity;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@ApiModelProperty(value = "生效日期")
	@TableField(value = "EFFECTIVE_DATE")
	private Date effectiveDate;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@ApiModelProperty(value = "到期日期")
	@TableField(value = "EXPIRATION_DATE")
	private Date expirationDate;

	@ApiModelProperty(value = "车辆正前方")
	@TableField(value = "FRONT_OF_CAR")
	private String frontOfCar;

	@ApiModelProperty(value = "左前45度角")
	@TableField(value = "LEFT_FRONT_45")
	private String leftFront45;

	@ApiModelProperty(value = "行驶证")
	@TableField(value = "DRIVING_LICENSE")
	private String drivingLicense;

	@ApiModelProperty(value = "右后45度角")
	@TableField(value = "RIGHT_REAR_45")
	private String rightRear45;

	@ApiModelProperty(value = "备注")
	@TableField(value = "REMARK")
	private String remark;

	@ApiModelProperty(value = "申请日期")
	@TableField(value = "APPLICATION_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date applicationDate;

	@ApiModelProperty(value = "验车时间")
	@TableField(value = "VEHICLE_INSPECT_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date vehicleInspectDate;

	@ApiModelProperty(value = "车架号")
	@TableField(value = "FRAME_NO")
	private String frameNo;

	@ApiModelProperty(value = "车架号照片")
	@TableField(value = "FRAME_NO_IMAGE")
	private String frameNoImage;

	@ApiModelProperty(value = "前挡玻璃")
	@TableField(value = "FRONT_GLASS")
	private String frontGlass;
}
