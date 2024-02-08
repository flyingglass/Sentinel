package com.alibaba.csp.sentinel.dashboard.repository.metric;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.MetricEntity;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author fly
 */
@Slf4j
@Component
public class InInfluxDbMetricsRepository implements MetricsRepository<MetricEntity> {

    @Resource
    private InfluxDB influxDB;

    @Override
    public void save(MetricEntity metric) {
        try {
            Point point = Point.measurement("metric")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("app", metric.getApp())
                    .tag("resource", metric.getResource())
                    .addField("gmtCreate", metric.getGmtCreate().getTime())
                    .addField("gmtModified", metric.getGmtModified().getTime())
                    .addField("timestamp", metric.getTimestamp().getTime())
                    .addField("passQps", metric.getPassQps())
                    .addField("successQps", metric.getSuccessQps())
                    .addField("blockQps", metric.getBlockQps())
                    .addField("exceptionQps", metric.getExceptionQps())
                    .addField("rt", metric.getRt())
                    .addField("count", metric.getCount())
                    .addField("resourceCode", metric.getResourceCode())
                    .build();
            influxDB.write(point);
        } catch (Exception e) {
            log.error("InfluxDbMetricRepository save error", e);
        }
    }

    @Override
    public void saveAll(Iterable<MetricEntity> metrics) {
        if (metrics == null) {
            return;
        }
        BatchPoints batchPoints = BatchPoints.builder()
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
        metrics.forEach(metric -> {
            Point point = Point.measurement("metric")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("app", metric.getApp())
                    .tag("resource", metric.getResource())
                    .addField("gmtCreate", metric.getGmtCreate().getTime())
                    .addField("gmtModified", metric.getGmtModified().getTime())
                    .addField("timestamp", metric.getTimestamp().getTime())
                    .addField("passQps", metric.getPassQps())
                    .addField("successQps", metric.getSuccessQps())
                    .addField("blockQps", metric.getBlockQps())
                    .addField("exceptionQps", metric.getExceptionQps())
                    .addField("rt", metric.getRt())
                    .addField("count", metric.getCount())
                    .addField("resourceCode", metric.getResourceCode())
                    .build();
            batchPoints.point(point);
        });
        influxDB.write(batchPoints);
    }

    @Override
    public List<MetricEntity> queryByAppAndResourceBetween(String app, String resource, long startTime, long endTime) {
        return null;
    }

    @Override
    public List<String> listResourcesOfApp(String app) {
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
