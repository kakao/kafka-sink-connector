package com.kakao.connector.kafka;

import com.kakao.connector.kafka.config.KafkaSinkConnectorConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KafkaSinkConnector extends SinkConnector {
    private final static Logger log = LoggerFactory.getLogger(KafkaSinkConnector.class);

    private Map<String, String> configProperties;

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> props) {
        this.configProperties = props;
        try {
            log.info("Connector props : " + props.toString());
            new KafkaSinkConnectorConfig(props);
        } catch (ConfigException e) {
            throw new ConnectException(e.getMessage(), e);
        }
    }

    @Override
    public Class<? extends Task> taskClass() {
        return KafkaSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> taskConfigs = new ArrayList<>();
        for (int i = 0; i < maxTasks; i++) {
            Map<String, String> taskProps = new HashMap<>();
            taskProps.putAll(configProperties);
            taskProps.put("task.id", Integer.toString(i));
            taskConfigs.add(taskProps);
        }
        return taskConfigs;
    }

    @Override
    public ConfigDef config() {
        return KafkaSinkConnectorConfig.CONFIG;
    }

    @Override
    public void stop() {
    }
}


