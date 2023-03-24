package org.xyg.eshop.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import okhttp3.Request;
import org.xyg.eshop.main.erpentity.AloSync;

public interface IAloSyncService extends IService<AloSync> {
	void sendCallError(Request request, String msg);

	void sendCallError(Request request, String msg, String sys);
}
