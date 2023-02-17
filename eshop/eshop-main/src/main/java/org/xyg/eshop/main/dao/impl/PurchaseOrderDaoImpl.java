package org.xyg.eshop.main.dao.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springrabbit.core.mp.base.BaseServiceImpl;
import org.springrabbit.core.tool.api.R;
import org.xyg.eshop.main.dao.IPurchaseOrderDao;
import org.xyg.eshop.main.dto.PurchaseOrderDTO;
import org.xyg.eshop.main.entity.PurchaseOrder;
import org.xyg.eshop.main.mapper.PurchaseOrderMapper;
import org.xyg.eshop.main.vo.ContractVO;
import org.xyg.eshop.main.vo.PurchaseOrderVO;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseOrderDaoImpl extends BaseServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements IPurchaseOrderDao {

}
