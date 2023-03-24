//package org.xyg.eshop.main.job;
//
//import org.xyg.eshop.main.entity.Member;
//import org.xyg.eshop.main.service.IMemberService;
//
//import java.util.concurrent.DelayQueue;
//
//public class TakeMember implements Runnable {
//
//	public static DelayQueue<ItemVo<Member>> delayMember = new DelayQueue<>();
//
//	private IMemberService memberService;
//
//	public TakeMember(IMemberService memberService) {
//		super();
//		this.memberService = memberService;
//	}
//
//	@Override
//	public void run() {
//		while (!Thread.currentThread().isInterrupted()) {
//			try {
//				ItemVo<Member> itemMember = delayMember.take();
//				if(itemMember != null) {
//					memberService.checkDelayExpireMember(itemMember.getData());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
