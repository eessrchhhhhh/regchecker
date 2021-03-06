package com.jisucloud.clawler.regagent.service.impl.trip;

import com.deep077.spiderbase.selenium.mitm.AjaxHook;
import com.deep077.spiderbase.selenium.mitm.ChromeAjaxHookDriver;
import com.deep077.spiderbase.selenium.mitm.HookTracker;

import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import java.util.Map;


@Slf4j
@PapaSpiderConfig(
		home = "booking.com", 
		message = "Booking.com-缤客,总部位于荷兰的住宿预订平台:遍布全球229个国家地区的14万个目的地,提供酒店,民宿,公寓,度假村,青旅等多种住宿选择.1.5亿+条真实住客点评帮您挑选好房源.多数客房可免费取消,支持在网页端和App在线管理订单.登陆后还可查看更多专属中国用户的隐藏优惠!", 
		platform = "booking", 
		platformName = "缤客酒店", 
		tags = { "旅游" , "酒店" , "美食" , "o2o" }, 
		testTelephones = { "13800100001", "18212345678" })
public class BookingSpider extends PapaSpider implements AjaxHook{

	private ChromeAjaxHookDriver chromeDriver;

	public boolean checkTelephone(String account) {
		try {
			chromeDriver = ChromeAjaxHookDriver.newNoHookInstance(true, false, CHROME_USER_AGENT);
			chromeDriver.get("https://www.booking.com/?aid=334565;label=baidu-brandzone_booking-brand-list1&utm_source=baidu&utm_medium=brandzone&utm_campaign=title");smartSleep(3000);
			chromeDriver.findElementById("current_account").click();smartSleep(3000);
			chromeDriver.findElementByCssSelector("#phone").sendKeys("+86"+account);
			chromeDriver.findElementByCssSelector("span.bui-button__text").click();smartSleep(3000);
			if (chromeDriver.getCurrentUrl().contains("sign-in/verify-phone")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (chromeDriver != null) {
				chromeDriver.quit();
			}
		}
		return checkTel;
	}

	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}

	@Override
	public HookTracker getHookTracker() {
		// TODO Auto-generated method stub
		return HookTracker.builder().addUrl("https://account.booking.com/sign-in").build();
	}

	@Override
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	boolean checkTel = false;

	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
	}

}
