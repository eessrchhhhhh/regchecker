package com.jisucloud.clawler.regagent.service.impl.social;

import com.jisucloud.clawler.regagent.service.PapaSpider;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PeiWoSpider implements PapaSpider {

	private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();

	@Override
	public String message() {
		return "陪我，是一款陌生人电话聊天app，作为一个纯粹关注声音的平台，陪我提出了前所未有的“声值社交”的概念,是基于通话的陌生人情感社交软件。 ";
	}

	@Override
	public String platform() {
		return "peiwo";
	}

	@Override
	public String home() {
		return "peiwo.cn";
	}

	@Override
	public String platformName() {
		return "陪我";
	}

	@Override
	public String[] tags() {
		return new String[] {"单身交友" , "婚恋"};
	}

//	public static void main(String[] args) throws InterruptedException {
//		System.out.println(new YueDongQuanSpider().checkTelephone("18210538000"));
//		System.out.println(new YueDongQuanSpider().checkTelephone("18210538513"));
//	}

	@Override
	public boolean checkTelephone(String account) {
		try {
			String url = "https://api.peiwo.cn/oauth/token";
			String postJson = "{\"timestamp\":"+System.currentTimeMillis()+",\"client_secret\":\"2CWMryRbkJgPa02AZDNifLqY0eBYWf\",\"client_id\":\"BP9wgoNbbbTp0PDve2i5WaPBRRHIcF\",\"grant_type\":\"password\",\"password\":\"c221f034030921962069f04cf4d8f3f2\",\"mobile\":\"86:"+account+"\"}";
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "okhttp/3.11.0")
					.addHeader("Host", "api.peiwo.cn")
					.addHeader("X-PW-Channel", "oppo")
					.addHeader("X-PW-Version", "300010")
					.addHeader("X-PW-Platform", "1")
					.addHeader("X-PW-DeviceID", "ffffffff-9215-c700-ffff-ffff99d603a9")
					.addHeader("Authorization", "")
					.post(FormBody.create(MediaType.get("application/json; charset=utf-8"), postJson))
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String res = response.body().string();
			if (res.contains("40006")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
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