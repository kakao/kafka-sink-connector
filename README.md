# kafka-sink-connector

kafka-sink-connector is a Kafka Connect connector that reads JSON data from Apache Kafka and send JSON record to Another Kafka topic.

## Features

- Sink Record to Another Kafka topic
- Data Filtering by [JsonPath](https://github.com/json-path/JsonPath)
- Sampling
- Timestamp parsing 
- Message key parsing
- Additional producer option(linger.ms, batch.size)

## Configuration Properties

| Required | Name                      | Type    | Description                                                             | Sample              |
|----------|---------------------------|---------|-------------------------------------------------------------------------|---------------------|
| O        | topics                    | String  | A list of Kafka topics that the sink connector watches.                 | my-topic            |
| O        | kafka.sink.topic          | String  | The Kafka topic name to which the sink connector writes.                | relay-topic         |
| O        | kafka.sink.bootstrap      | String  | The Kafka bootstrap server to which the sink connector writes.         | my-kafka:9092       |
| O        | kafka.filtering.condition | String  | Filtering condition for value fields using [json-path](https://github.com/json-path/JsonPath).                   | $.[?(  !@.all )]    |
| X        | kafka.timestamp.enabled   | Boolean | This feature is injects a Timestamp into the Kafka Record               | true                |
| X        | kafka.timestamp.field     | String  | Location of the json field in the Timestamp to inject into the Record   | @.timestamp         |
| X        | kafka.timestamp.format    | String  | Format of timestamps to inject into the Record                          | yyyyMMdd HHmmss SSS |
| X        | kafka.sampling.enabled    | Boolean | This feature enables whether sampling is applied.                       | true                |
| X        | kafka.sampling.percentage | String  | Sampling percentage value                                               | 0.5                 |
| X        | kafka.key.enabled         | Boolean | This feature is injects a Message Key into the Kafka Record             | true                |
| X        | kafka.key.field           | String  | Location of the json field in the Message Key to inject into the Record | @.name              |
| X        | kafka.producer.linger.ms  | String  | Producer linger.ms option                                               | 10                  |
| X        | kafka.producer.batch.size | String  | Producer batch.size option                                              | 100000              |


### Configuration Example

<table>
<thead> 
    <tr >
        <td>Source Kafka<br>(fruits topic)</td>
        <td>→</td>
        <td>kafka-sink-connector</td>
        <td>→</td>
        <td>Sink Kafka<br>(red-color-fruit topic)</td>
    </tr></thead>
    <tbody>
    <tr>
        <td><pre>{
    "color":"red",
    "name":"apple"
}</pre><pre>{
    "color":"yellow",
    "name":"banana"
}</pre><pre>{
    "color":"orange",
    "name":"tangerine"
}</pre></td>
        <td>→</td>
        <td><pre>{
    "name": "red-color-filtering-test",
    "config":
    {
        "connector.class": "com.kakao.connector.kafka.KafkaSinkConnector",
        "kafka.filtering.condition": "$.[?(@.color == 'red')]",
        "kafka.sink.bootstrap": "color-kafka:9092",
        "kafka.sink.topic": "red-color-fruit",
        "tasks.max": "1",
        "topics": "fruits"
    }
}</pre></td>
        <td>→</td>
        <td><pre>{
    "color":"red",
    "name":"apple"
}</pre></td>
    </tr></tbody>
</table>


## Build 

```
$ git clone https://github.daumkakao.com/adrec/kafka-sink-connector.git
$ cd kafka-sink-connector
$ ./gradlew uberJar
$ ls build/libs 
kafka-sink-connector-1.0.jar
```

## How to add kafka-sink-connector JAR file in Kafka Connect worker?

- Installing Connect Plugins : https://docs.confluent.io/kafka-connectors/self-managed/userguide.html#installing-kconnect-plugins
- `plugin.path` : https://kafka.apache.org/documentation/#connectconfigs_plugin.path

## License

This software is licensed under the [Apache 2 license](LICENSE), quoted below.

Copyright 2023 Kakao Corp. <http://www.kakaocorp.com>

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this project except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
