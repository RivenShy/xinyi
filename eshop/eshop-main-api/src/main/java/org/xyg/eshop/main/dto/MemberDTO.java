package org.xyg.eshop.main.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.xyg.eshop.main.entity.Member;

import java.io.Serializable;
import java.util.List;

@Data
public class MemberDTO extends Member implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "会员状态查询列表")
	private List<Integer> memeberStatusList;
}
