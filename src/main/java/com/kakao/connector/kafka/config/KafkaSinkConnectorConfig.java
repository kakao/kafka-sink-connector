package com.kakao.connector.kafka.config;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Map;

public class KafkaSinkConnectorConfig extends AbstractConfig {

    public static final String SOURCE_TOPIC = "topics";
    public static final String SOURCE_TOPIC_DEFAULT_VALUE = "my-topic";
    public static final String SOURCE_TOPIC_DOC = "Define source topic";

    public static final String SINK_TOPIC = "kafka.sink.topic";
    public static final String SINK_TOPIC_DEFAULT_VALUE = "test";
    public static final String SINK_TOPIC_DOC = "Define sink topic";

    public static final String FILTERING_CONDITION = "kafka.filtering.condition";
    public static final String FILTERING_CONDITION_DEFAULT_VALUE = "$.[?( !@.all )]";
    public static final String FILTERING_CONDITION_DOC = "Define filtering condition";

    public static final String SINK_BOOTSTRAP_SERVER = "kafka.sink.bootstrap";
    public static final String SINK_BOOTSTRAP_SERVER_DEFAULT_VALUE = "second-kafka:9092";
    public static final String SINK_BOOTSTRAP_SERVER_DOC = "Define sink bootstrap";

    public static final String TIMESTAMP_PARSING_ENABLED = "kafka.timestamp.enabled";
    public static final boolean TIMESTAMP_PARSING_ENABLED_DEFAULT_VALUE = false;
    public static final String TIMESTAMP_PARSING_ENABLED_DOC = "Enable parsing timestamp";

    public static final String TIMESTAMP_PARSING_FIELD = "kafka.timestamp.field";
    public static final String TIMESTAMP_PARSING_FIELD_DEFAULT_VALUE = "@.timestamp";
    public static final String TIMESTAMP_PARSING_FIELD_DOC = "Define timestamp field";

    public static final String TIMESTAMP_PARSING_FORMAT = "kafka.timestamp.format";
    public static final String TIMESTAMP_PARSING_FORMAT_DEFAULT_VALUE = "yyyy-MM-dd";
    public static final String TIMESTAMP_PARSING_FORMAT_DOC = "Define timestamp format";


    public static final String SAMPLING_ENABLED= "kafka.sampling.enabled";
    public static final boolean SAMPLING_ENABLED_DEFAULT_VALUE = false;
    public static final String SAMPLING_ENABLED_DOC = "Enable sampling";

    public static final String SAMPLING = "kafka.sampling.percentage";
    public static final String SAMPLING_DEFAULT_VALUE = "0.1";
    public static final String SAMPLING_DOC = "Define sampling percentage";


    public static final String MESSAGE_KEY_PARSING_ENABLED= "kafka.key.enabled";
    public static final boolean MESSAGE_KEY_PARSING_ENABLED_DEFAULT_VALUE = false;
    public static final String MESSAGE_KEY_PARSING_ENABLED_DOC = "Enable key parsing enabled";

    public static final String MESSAGE_KEY_PARSING_FIELD = "kafka.key.field";
    public static final String MESSAGE_KEY_PARSING_FIELD_DEFAULT_VALUE = "@.timestamp";
    public static final String MESSAGE_KEY_PARSING_FIELD_DOC = "Define key field";


    public static final String PRODUCER_LINGER_MS = "kafka.producer.linger.ms";
    public static final String PRODUCER_LINGER_MS_DEFAULT_VALUE = "0";
    public static final String PRODUCER_LINGER_MS_DOC = "Set producer linger.ms option";

    public static final String PRODUCER_BATCH_SIZE = "kafka.producer.batch.size";
    public static final String PRODUCER_BATCH_SIZE_DEFAULT_VALUE = "16384";
    public static final String PRODUCER_BATCH_SIZE_DOC = "Set producer batch.size option";


    public static ConfigDef CONFIG = new ConfigDef()
            // filtering
            .define(SOURCE_TOPIC, Type.STRING, SOURCE_TOPIC_DEFAULT_VALUE, Importance.HIGH, SOURCE_TOPIC_DOC)
            .define(SINK_TOPIC, Type.STRING, SINK_TOPIC_DEFAULT_VALUE, Importance.HIGH, SINK_TOPIC_DOC)
            .define(FILTERING_CONDITION, Type.STRING, FILTERING_CONDITION_DEFAULT_VALUE, Importance.HIGH, FILTERING_CONDITION_DOC)
            .define(SINK_BOOTSTRAP_SERVER, Type.STRING, SINK_BOOTSTRAP_SERVER_DEFAULT_VALUE, Importance.HIGH, SINK_BOOTSTRAP_SERVER_DOC)
            // timestamp parsing
            .define(TIMESTAMP_PARSING_ENABLED, Type.BOOLEAN, TIMESTAMP_PARSING_ENABLED_DEFAULT_VALUE, Importance.MEDIUM, TIMESTAMP_PARSING_ENABLED_DOC)
            .define(TIMESTAMP_PARSING_FIELD, Type.STRING, TIMESTAMP_PARSING_FIELD_DEFAULT_VALUE, Importance.MEDIUM, TIMESTAMP_PARSING_FIELD_DOC)
            .define(TIMESTAMP_PARSING_FORMAT, Type.STRING, TIMESTAMP_PARSING_FORMAT_DEFAULT_VALUE, Importance.MEDIUM, TIMESTAMP_PARSING_FORMAT_DOC)
            // sampling
            .define(SAMPLING_ENABLED, Type.BOOLEAN, SAMPLING_ENABLED_DEFAULT_VALUE, Importance.MEDIUM, SAMPLING_ENABLED_DOC)
            .define(SAMPLING, Type.DOUBLE, SAMPLING_DEFAULT_VALUE, Importance.MEDIUM, SAMPLING_DOC)
            // message key parsing
            .define(MESSAGE_KEY_PARSING_ENABLED, Type.BOOLEAN, MESSAGE_KEY_PARSING_ENABLED_DEFAULT_VALUE, Importance.MEDIUM, MESSAGE_KEY_PARSING_ENABLED_DOC)
            .define(MESSAGE_KEY_PARSING_FIELD, Type.STRING, MESSAGE_KEY_PARSING_FIELD_DEFAULT_VALUE, Importance.MEDIUM, MESSAGE_KEY_PARSING_FIELD_DOC)
            // producer additional options for batch send
            .define(PRODUCER_LINGER_MS, Type.STRING, PRODUCER_LINGER_MS_DEFAULT_VALUE, Importance.MEDIUM, PRODUCER_LINGER_MS_DOC)
            .define(PRODUCER_BATCH_SIZE, Type.STRING, PRODUCER_BATCH_SIZE_DEFAULT_VALUE, Importance.MEDIUM, PRODUCER_BATCH_SIZE_DOC)
            ;

    public KafkaSinkConnectorConfig(Map<String, String> props) {
        super(CONFIG, props);
    }
}
