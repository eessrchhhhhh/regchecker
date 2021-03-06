package com.jisucloud.clawler.regagent.service.impl.util;

import com.deep077.spiderbase.selenium.mitm.ChromeAjaxHookDriver;

import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;
import com.jisucloud.clawler.regagent.util.OCRDecode;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;


import org.openqa.selenium.WebElement;

@Slf4j
@PapaSpiderConfig(
		home = "uc.cn", 
		message = "UC浏览器是全球6亿用户共同选择的智能手机浏览器,登陆UC官网免费下载UC浏览器安卓版/iPhone版,给您超快感的上网体验。", 
		platform = "uc", 
		platformName = "UC浏览器", 
		tags = { "工具", "浏览器" }, 
		testTelephones = { "13910000000", "18212345678" })
public class UCSpider extends PapaSpider {
	
	private ChromeAjaxHookDriver chromeDriver;
	private boolean checkTel = false;
	private boolean vcodeSuc = false;//验证码是否正确
	
	private String getImgCode() {
		for (int i = 0 ; i < 3; i++) {
			try {
				WebElement img = chromeDriver.findElementByCssSelector("#captchaImg img");
				img.click();smartSleep(1000);
				byte[] body = chromeDriver.screenshot(img);
				return OCRDecode.decodeImageCode(body);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}


	public boolean checkTelephone(String account) {
		try {
			chromeDriver = ChromeAjaxHookDriver.newChromeInstance(false, true);
			chromeDriver.get("https://api.open.uc.cn/cas/forgotpassword/forgotPasswordCommit?client_id=4&redirect_uri=http%3A%2F%2Fid.uc.cn%2F&display=pc&change_uid=0");smartSleep(2000);
			WebElement nameInputArea = chromeDriver.findElementByCssSelector("#loginName");
			nameInputArea.sendKeys(account);
			for (int i = 0 ; i < 5 ; i ++) {
				WebElement captcha = chromeDriver.findElementByCssSelector("#captchaVal");
				captcha.clear();
				captcha.sendKeys(getImgCode());
				chromeDriver.findElementByCssSelector("#submit_btn").click();smartSleep(3000);
				if (chromeDriver.checkElement("#dForm2")) {
					return true;
				}
				String dCaptchaValError = chromeDriver.findElementById("dCaptchaValError").getText();
				if (dCaptchaValError.contains("错误")){
					continue;
				}
				break;
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

}
