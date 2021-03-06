package com.jisucloud.clawler.regagent.service.impl.game;


import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;


import java.util.Map;


@Slf4j
@PapaSpiderConfig(
		home = "sdo.com", 
		message = "上海盛大网络发展有限公司，作为领先的互动娱乐媒体企业，盛大网络通过盛大游戏、盛大文学、盛大在线等主体和其它业务，向广大用户提供多元化的互动娱乐内容和服务。", 
		platform = "sdo", 
		platformName = "盛大网络", 
		tags = { "游戏" }, 
		testTelephones = { "18210530000", "13269423806" })
public class ShengDaSpider extends PapaSpider {

	public boolean checkTelephone(String account) {
		for (int i = 0 ; i < 10 ; i++) {
			String url = "https://cas.sdo.com/authen/checkAccountType.jsonp?callback=checkAccountType_JSONPMethod&serviceUrl=register.sdo.com&appId=991002500&areaId=201000&authenSource=2&inputUserId="+account+"&locale=zh_CN&productId=1&productVersion=1.7&version=21&_=" + System.currentTimeMillis();
			try {
				Request request = new Request.Builder().url(url)
						.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
						.header("Host", "cas.sdo.com")
						.header("Referer", "http://register.sdo.com/register/index?appId=991002500&areaId=201000")
						.build();
				Response response = okHttpClient.newCall(request).execute();
				String res = response.body().string();
				if (res.contains("recommendLoginType\": 15")) {
					return true;
				}else if (res.contains("recommendLoginType")) {
					return false;
				}
				smartSleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
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
