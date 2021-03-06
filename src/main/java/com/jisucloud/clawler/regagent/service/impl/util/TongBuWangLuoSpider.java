package com.jisucloud.clawler.regagent.service.impl.util;


import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;


@Slf4j
@PapaSpiderConfig(
		home = "tongbu.com", 
		message = "同步网络官网提供同步助手、同步推、越狱助手下载。同步助手是史上最安全最易用的苹果手机助手；同步推是最受欢迎的游戏应用下载平台，可免越狱，免Apple ID，海量资源免费下载；越狱助手支持一键完", 
		platform = "tongbu", 
		platformName = "同步网络", 
		tags = { "工具" , "手机越狱", "手机助手" }, 
		testTelephones = { "13695286288", "18212345678" })
public class TongBuWangLuoSpider extends PapaSpider {


	public boolean checkTelephone(String account) {
		try {
			String url = "https://id.tongbu.com/forget/accountExist";
			FormBody formBody = new FormBody
	                .Builder()
	                .add("account", account)
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Referer", "https://id.tongbu.com/forget/verify")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			if (response.body().string().contains("0")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
