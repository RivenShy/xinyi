package org.xyg.eshop.main.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xyg.eshop.main.dto.ContractDTO;
import org.xyg.eshop.main.entity.Contract;
import org.xyg.eshop.main.vo.ContractVO;

import java.util.List;

@Mapper
public interface ContractMapper extends BaseMapper<Contract> {

	@SqlParser(filter=true)
	List<ContractVO> selectContractPage(IPage page, @Param("contract") ContractDTO contractDTO);


	ContractVO selectById(Long id);
}
