package org.xyg.eshop.main.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@ApiModel(value = "文件导入" , description = "文件导入")
public class FileEntityUtil {
	@ApiModelProperty(value = "文件数据")
	private MultipartFile file;
	@ApiModelProperty(value = "头ID")
	private Long headId;
}
