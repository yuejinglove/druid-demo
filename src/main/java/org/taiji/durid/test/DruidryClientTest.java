package org.taiji.durid.test;

import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.taiji.durid.config.DruidConfig;

import in.zapr.druid.druidry.Interval;
import in.zapr.druid.druidry.client.DruidClient;
import in.zapr.druid.druidry.client.DruidConfiguration;
import in.zapr.druid.druidry.client.DruidJerseyClient;
import in.zapr.druid.druidry.client.DruidQueryProtocol;
import in.zapr.druid.druidry.client.exception.ConnectionException;
import in.zapr.druid.druidry.client.exception.QueryException;
import in.zapr.druid.druidry.filter.searchQuerySpec.InsensitiveContainsSearchQuerySpec;
import in.zapr.druid.druidry.filter.searchQuerySpec.SearchQuerySpec;
import in.zapr.druid.druidry.granularity.PredefinedGranularity;
import in.zapr.druid.druidry.granularity.SimpleGranularity;
import in.zapr.druid.druidry.query.search.DruidSearchQuery;

/**
 * @author zhengyh
 * @Type DruidryClientTest
 * @time 2018-10-17 下午6:32:16
 * @description 使用Druidry进行查询
 * @version v1.0
 * @TODO TODO
 */
public class DruidryClientTest {
	private DruidConfiguration druidConfiguration;
	private DruidClient client ;
    @Before
    public void init() {
        druidConfiguration = DruidConfiguration
				.builder()
                .protocol(DruidQueryProtocol.HTTP)
                .host(DruidConfig.HOST)
                .port(DruidConfig.PORT)
                .endpoint("druid/v2/")
                .concurrentConnectionsRequired(8)
                .build();
        client = new DruidJerseyClient(druidConfiguration);
    }
    
    /**
     * @author zhengyh
     * @descript Query Search:
{
	"queryType": "search",
	"dataSource": "PI2KAFKA_PRESSURE_TEST",
	"granularity": "day",
	"query": {
	  "type": "insensitive_contains",
	  "value": "DCS2.20LAC20CT308"
	},
	"intervals": "2018-09-25T08:35:20+00:00/2018-10-10T08:35:20+00:00"
}
     */
    @Test
	public void queryTest() {
		
		SearchQuerySpec searchQuerySpec = new InsensitiveContainsSearchQuerySpec("DCS2.20LAC20CT308");

        DateTime startTime = new DateTime(2018, 9, 25, 8,
                35, 20, DateTimeZone.UTC);
        DateTime endTime = new DateTime(2018, 10, 10, 8,
                35, 20, DateTimeZone.UTC);
        Interval interval = new Interval(startTime, endTime);

        DruidSearchQuery query = DruidSearchQuery.builder()
                .dataSource("PI2KAFKA_PRESSURE_TEST")
                .granularity(new SimpleGranularity(PredefinedGranularity.DAY))
                .query(searchQuerySpec)
                .intervals(Collections.singletonList(interval))
                .build();
        
		try {
			this.client.connect();
			String result = this.client.query(query);
			System.out.println(result);
		} catch (ConnectionException e) {
			e.printStackTrace();
		}catch (QueryException e) {
			e.printStackTrace();
		}finally {
			try {
				this.client.close();
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
		
	}

}
