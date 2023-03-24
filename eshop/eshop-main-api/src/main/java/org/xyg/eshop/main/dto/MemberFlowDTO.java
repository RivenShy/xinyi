package org.xyg.eshop.main.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.MemberFlow;

import java.io.Serializable;
import java.util.Date;

@Data
public class MemberFlowDTO extends MemberFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@ApiModelProperty(value = "到期日期")
	private Date expirationDate;
}
