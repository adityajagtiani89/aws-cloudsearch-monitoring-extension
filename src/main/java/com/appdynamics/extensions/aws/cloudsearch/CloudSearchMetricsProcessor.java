package com.appdynamics.extensions.aws.cloudsearch;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.appdynamics.extensions.aws.config.MetricType;
import com.appdynamics.extensions.aws.metric.NamespaceMetricStatistics;
import com.appdynamics.extensions.aws.metric.StatisticType;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessor;
import com.appdynamics.extensions.aws.metric.processors.MetricsProcessorHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Satish Muddam
 */
public class CloudSearchMetricsProcessor implements MetricsProcessor {

    private static final String NAMESPACE = "AWS/CloudSearch";

    private static final String[] DIMENSIONS = {"DomainName"};

    private List<MetricType> metricTypes;

    private Pattern excludeMetricsPattern;

    public CloudSearchMetricsProcessor(List<MetricType> metricTypes,
                                       Set<String> excludeMetrics) {
        this.metricTypes = metricTypes;
        this.excludeMetricsPattern = MetricsProcessorHelper.createPattern(excludeMetrics);
    }

    public List<Metric> getMetrics(AmazonCloudWatch awsCloudWatch) {
        return MetricsProcessorHelper.getFilteredMetrics(awsCloudWatch,
                NAMESPACE,
                excludeMetricsPattern,
                DIMENSIONS);
    }

    public StatisticType getStatisticType(Metric metric) {
        return MetricsProcessorHelper.getStatisticType(metric, metricTypes);
    }

    public Map<String, Double> createMetricStatsMapForUpload(NamespaceMetricStatistics namespaceMetricStats) {
        Map<String, String> dimensionToMetricPathNameDictionary = new HashMap<String, String>();
        dimensionToMetricPathNameDictionary.put(DIMENSIONS[0], "Domain Name");


        return MetricsProcessorHelper.createMetricStatsMapForUpload(namespaceMetricStats,
                dimensionToMetricPathNameDictionary, false);
    }

    public String getNamespace() {
        return NAMESPACE;
    }

}
