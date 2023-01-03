package com.kakao.connector.kafka;

import com.jayway.jsonpath.JsonPath;
import com.kakao.connector.kafka.config.KafkaProducerConfig;
import com.kakao.connector.kafka.config.KafkaSinkConnectorConfig;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class KafkaSinkTask extends SinkTask {
    private final static Logger log = LoggerFactory.getLogger(KafkaSinkTask.class);
    private KafkaProducer<String, String> producer;
    private String sinkTopic;
    private String connectorName;
    private String filteringCondition;

    private boolean timestampParsingEnabled;
    private String timestampParsingField;
    private String timestampFormat;

    private boolean samplingEnabled;
    private double samplingPercentage;

    private boolean keyParsingEnabled;
    private String keyParsingField;

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> props) {
        try {
            log.info("Task config setting : " + props.toString());
            KafkaSinkConnectorConfig config;
            connectorName = props.get("name");
            config = new KafkaSinkConnectorConfig(props);
            sinkTopic = config.getString(config.SINK_TOPIC);
            filteringCondition = config.getString(config.FILTERING_CONDITION);

            // set timestamp parsing
            timestampParsingEnabled = config.getBoolean(config.TIMESTAMP_PARSING_ENABLED);
            if (timestampParsingEnabled) {
                timestampParsingField = config.getString(config.TIMESTAMP_PARSING_FIELD);
                timestampFormat = config.getString(config.TIMESTAMP_PARSING_FORMAT);
            }

            // set sampling
            samplingEnabled = config.getBoolean(config.SAMPLING_ENABLED);
            if (samplingEnabled)
                samplingPercentage = config.getDouble(config.SAMPLING);

            // set message key
            keyParsingEnabled = config.getBoolean(config.MESSAGE_KEY_PARSING_ENABLED);
            if (keyParsingEnabled)
                keyParsingField = config.getString(config.MESSAGE_KEY_PARSING_FIELD);

            // set filtering producer
            String bootstrapServer = config.getString(config.SINK_BOOTSTRAP_SERVER);
            String producerLingerMs = config.getString(config.PRODUCER_LINGER_MS);
            String producerBatchSize = config.getString(config.PRODUCER_BATCH_SIZE);
            Properties producerProperties = KafkaProducerConfig.producer(bootstrapServer);
            producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, producerLingerMs);
            producerProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, producerBatchSize);
            producer = new KafkaProducer<>(producerProperties);

        } catch (Exception e) {
            throw new ConnectException(e.getMessage(), e);
        }

    }

    @Override
    public void put(Collection<SinkRecord> records) {
        if (samplingEnabled) {
            for (SinkRecord record : records) {
                if (record.value() != null) {
                    try {
                        String value = record.value().toString();
                        boolean samplingCondition = Math.random() < samplingPercentage;
                        if (samplingCondition
                                && isFilteringMatch(value)
                        ) {
                            sendRecord(value);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage() + " / " + connectorName, e);
                    }
                }
            }
        } else {
            for (SinkRecord record : records) {
                if (record.value() != null) {
                    try {
                        String value = record.value().toString();
                        if (isFilteringMatch(value)) {
                            sendRecord(value);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage() + " / " + connectorName, e);
                    }
                }
            }
        }
    }

    private void sendRecord(String value) {
        ProducerRecord<String, String> sendRecord = getSinkRecord(value);
        producer.send(sendRecord, new ProducerCallback());
    }

    private ProducerRecord<String, String> getSinkRecord(String value) {
        String messageKey = keyParsingEnabled ? parsingMessageKey(value) : null;
        if (timestampParsingEnabled) {
            return new ProducerRecord<>(sinkTopic, null, parsingTimestamp(value), messageKey, value);
        } else {
            return new ProducerRecord<>(sinkTopic, messageKey, value);
        }
    }

    private boolean isFilteringMatch(String value) {
        // condition에 해당하는 데이터가 아니면 []를 반환한다. -> []이면 대상 데이터가 아니므로 PASS
        return !JsonPath.read(value, filteringCondition).toString().equals("[]");
    }

    private long parsingTimestamp(String value) {
        try {
            String timestampFieldValue = JsonPath.read(value, timestampParsingField).toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timestampFormat);
            LocalDateTime dateTime = LocalDateTime.parse(timestampFieldValue, formatter);
            return Timestamp.valueOf(dateTime).getTime();
        } catch (Exception e) {
            log.error(e.getMessage() + " / " + connectorName, e);
            return System.currentTimeMillis();
        }
    }

    private String parsingMessageKey(String value) {
        try {
            StringBuilder messageKey = new StringBuilder();
            List<String> fields = Arrays.asList(keyParsingField.split(","));
            for (String field : fields) {
                messageKey.append(JsonPath.read(value, field).toString());
            }
            return messageKey.toString();
        } catch (Exception e) {
            log.error(e.getMessage() + " / " + connectorName, e);
            return null;
        }
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {
    }

    @Override
    public void stop() {
        producer.flush();
        producer.close();
    }
}