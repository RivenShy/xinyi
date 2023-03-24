package org.xyg.eshop.main.erpentity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("alo_sync_records")
public class AloSync implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.NONE)
	private String url;
	private Date lastTime;
}
