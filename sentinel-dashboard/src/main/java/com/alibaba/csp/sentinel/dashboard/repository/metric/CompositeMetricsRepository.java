package com.alibaba.csp.sentinel.dashboard.repository.metric;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.MetricEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author fly
 */
@Component
public class CompositeMetricsRepository implements MetricsRepository<MetricEntity> {

    private List<MetricsRepository<MetricEntity>> repositories = Collections.emptyList();

    public CompositeMetricsRepository(List<MetricsRepository<MetricEntity>> repositories) {
        if (repositories != null && repositories.size() > 0) {
            repositories.sort(Comparator.comparingInt(MetricsRepository::getOrder));
            this.repositories = repositories;
        }
    }

    @Override
    public void save(MetricEntity metric) {
        for (MetricsRepository<MetricEntity> repository: repositories){
            repository.save(metric);
        }
    }

    @Override
    public void saveAll(Iterable<MetricEntity> metrics) {
        for (MetricsRepository<MetricEntity> repository: repositories){
            repository.saveAll(metrics);
        }
    }

    @Override
    public List<MetricEntity> queryByAppAndResourceBetween(String app, String resource, long startTime, long endTime) {
        for (MetricsRepository<MetricEntity> repository : repositories) {
            List<MetricEntity> result = repository.queryByAppAndResourceBetween(app, resource, startTime, endTime);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public List<String> listResourcesOfApp(String app) {
        for (MetricsRepository<MetricEntity> repository: repositories){
            List<String> result = repository.listResourcesOfApp(app);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
