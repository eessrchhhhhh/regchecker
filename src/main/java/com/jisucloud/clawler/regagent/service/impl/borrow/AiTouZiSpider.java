package com.jisucloud.clawler.regagent.service.impl.borrow;

import com.deep077.spiderbase.selenium.mitm.AjaxHook;
import com.deep077.spiderbase.selenium.mitm.ChromeAjaxHookDriver;
import com.deep077.spiderbase.selenium.mitm.HookTracker;

import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;
import com.jisucloud.clawler.regagent.util.OCRDecode;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import org.openqa.selenium.WebElement;

import java.util.Map;


@Slf4j
@PapaSpiderConfig(
		home = "itouzi.com", 
		message = "最安全规范、实力更强的互联网金融投资理财平台,知名实力P2P网贷品牌,丰富多样且本息全额保障的高收益理财产品,大众化的低投资门槛,全免费的服务体验,爱投资,值得爱。", 
		platform = "itouzi", 
		platformName = "爱投资", 
		tags = { "P2P", "借贷" }, 
		testTelephones = { "13879580000", "18212345678" },
		exclude = true , excludeMsg = "图片验证码太复杂")
public class AiTouZiSpider extends PapaSpider implements AjaxHook {
	
	private ChromeAjaxHookDriver chromeDriver;
	
	private boolean checkTelephone = false;
	
	//暂时不能访问此页面，被反扒
	public boolean success = false;//默认false
	
	private String getImgCode() {
		if (chromeDriver.checkElement("input[name='valicode']")) {
			for (int i = 0 ; i < 3; i++) {
				try {
					//windows下图片未正确获取
					WebElement img = chromeDriver.findElementByCssSelector("#pwd_login img[class='img-captch']");
					img.click();
					smartSleep(1000);
					byte[] body = chromeDriver.screenshot(img);
					return OCRDecode.decodeImageCode(body);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}


	public boolean checkTelephone(String account) {
		try {
			chromeDriver = ChromeAjaxHookDriver.newChromeInstance(false, false);
			chromeDriver.addAjaxHook(this);
			chromeDriver.get("https://www.itouzi.com/login");
			smartSleep(2000);
			chromeDriver.findElementByCssSelector("#pwd_login input[name='username']").sendKeys(account);
			chromeDriver.findElementByCssSelector("#pwd_login input[name='password']").sendKeys("xasp12nxoanx89");
			for (int i = 0; i < 5; i++) {
				String imageCode = getImgCode();
				if (!imageCode.isEmpty()) {
					WebElement codeInput = chromeDriver.findElementByCssSelector("input[name='valicode']");
					codeInput.clear();
					codeInput.sendKeys(imageCode);
				}
				WebElement next = chromeDriver.findElementByCssSelector("#pwd_login button[class='btn btn-block btn-auto btn-hue2 login']");
				next.click();
				smartSleep(5000);
				if (success) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (chromeDriver != null) {
				chromeDriver.quit();
			}
		}
		return checkTelephone;
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
		return HookTracker.builder().addUrl("user/ajax/login").build();
	}

	@Override
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		return null;
	}

	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		String responseText = contents.getTextContents();
		if (responseText.contains("code\":5") || responseText.contains("账号或密码错误") || responseText.contains("锁定")) {
			success = true;
			checkTelephone = responseText.contains("次机会") || responseText.contains("锁定");
		}
	}

}
