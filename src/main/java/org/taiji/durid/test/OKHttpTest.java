/**
 * 
 */
package org.taiji.durid.test;

import java.io.IOException;

import org.junit.Test;
import org.taiji.durid.config.DruidConfig;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhengyh
 * @Type OKTest
 * @time 2018-10-17 下午3:56:42
 * @description 通过Http连接进行json Api查询
 * @version v1.0
 * @TODO TODO
 */
public class OKHttpTest {

	/**
	 * @author zhengyh
	 * @descript 通过OKHttp 查询json接口
	 * @param args
	 */
	@Test
	public void HttpQuery() {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\r\n    \"dimensions\": [\r\n        \"Value\",\r\n        \"TagName\"\r\n    ],\r\n    \"pagingSpec\": {\r\n        \"pagingIdentifiers\": {},\r\n        \"threshold\": 10\r\n    },\r\n    \"intervals\": \"2018-09-25T08:35:20+00:00/2018-09-27T08:35:20+00:00\",\r\n    \"dataSource\": \"PI2KAFKA_PRESSURE_TEST\",\r\n    \"granularity\": \"all\",\r\n    \"filter\": {\r\n        \"type\": \"selector\",\r\n        \"dimension\": \"TagName\",\r\n        \"value\": \"DCS2.20LAC20CT308\"\r\n    },\r\n    \"context\": {\r\n        \"skipEmptyBuckets\": \"true\"\r\n    },\r\n    \"queryType\": \"select\"\r\n}");
		Request request = new Request.Builder()
		  .url("http://"+DruidConfig.HOST+":8082/druid/v2/")
		  .post(body)
		  .addHeader("Content-Type", "application/json")
		  .build();

		try {
			Response response = client.newCall(request).execute();
			System.out.println(response.toString());
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
