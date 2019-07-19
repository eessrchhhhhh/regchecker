package com.jisucloud.clawler.regagent.service.impl.work;

import com.google.common.collect.Sets;
import com.jisucloud.clawler.regagent.service.PapaSpider;
import com.jisucloud.clawler.regagent.service.UsePapaSpider;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@UsePapaSpider
public class DingDanHuiSpider extends PapaSpider {

	private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();

	@Override
	public String message() {
		return "订单汇是通过SAAS云部署（无须下载安装维护）和多终端（PC端、手机APP）操作的方式，为中小微企业提供在线进销存财管理系统和交易工具服务，解决广大企业销售与管理脱钩的痛点，拉近企业与客户距离。";
	}

	@Override
	public String platform() {
		return "ddhing";
	}

	@Override
	public String home() {
		return "ddhing.com";
	}

	@Override
	public String platformName() {
		return "订单汇";
	}

	@Override
	public String[] tags() {
		return new String[] {"求职" , "招聘"};
	}
	
	@Override
	public Set<String> getTestTelephones() {
		return Sets.newHashSet("18515290000", "15008276300");
	}

	@Override
	public boolean checkTelephone(String account) {
		try {
			String url = "https://ddhing.com/account/ddhUserCheck?mobile="+account+"&_=" +System.currentTimeMillis();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Referer", "https://ddhing.com/account/DdhRegister")
					.build();
			Response response = okHttpClient.newCall(request).execute();
			String res = response.body().string();
			if (res.contains("false")) {
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