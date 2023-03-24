//package org.xyg.eshop.main.job;
//
//import lombok.Data;
//
//import java.util.concurrent.Delayed;
//import java.util.concurrent.TimeUnit;
//
//@Data
//public class ItemVo<T> implements Delayed {
//
////	 到期时间，传入的数值表示过期的时长，单位：ms
//	private long expireTime;
//
////	业务实体-泛型
//	private T data;
//
//	public ItemVo(long expireTime, T data) {
//		super();
//		// 传入的时间
//		this.expireTime = expireTime + System.currentTimeMillis();
//		this.data = data;
//	}
//
////	返回剩余时间
//	@Override
//	public long getDelay(TimeUnit unit) {
//		long time = unit.convert(this.expireTime - System.currentTimeMillis(), unit);
//		return time;
//	}
//
//	@Override
//	public int compareTo(Delayed o) {
//		long time = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
//		int result = (time == 0) ? 0 : ( (time < 0) ? -1 : 1 );
//		return result;
//	}
//}
