package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xyg.eshop.main.entity.ProductModelAttachment;
import org.xyg.eshop.main.vo.ProductModelAttachmentVO;

import java.time.LocalDateTime;

@Mapper
public interface ProductModelAttachmentMapper extends BaseMapper<ProductModelAttachment> {

	ProductModelAttachmentVO getDetail(Long id) ;

	LocalDateTime findMaxUpdateDate();
}
