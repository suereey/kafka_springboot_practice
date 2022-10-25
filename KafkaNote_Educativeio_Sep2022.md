# Kafka Basic

## Producer --> Consumer
Pub sub
Message queuing

## Components of Kafka
Message (in batch 批量发送 and compressed)

Topic

Parition
- Messages are ordered by time only within a partition and not across the entire topic.
- Messages are read from beginning to end in a partition.
- Message can only be appended to the end of a partition. (Notice the similarity to a **commit log**)
- Partitions allow Kafka to scale horizontally and also provide redundancy. 
- Each partition can be hosted on a different server, which allows new partitions to be added to a topic as the load on the system increases.

    ![](screenshot/09292022_KafkaEducativeio/Screen%20Shot%202022-09-29%20at%205.01.11%20PM.png)

Message Key

Message Offset

Schemas
- it is recommended that messages follow a structure and form that allows for easy understanding. Messages can be expressed in JSON, XML, Avro, or other formats.

**Broker: A single Kafka server is called a broker. Usually, several Kafka brokers operate as one Kafka cluster.**
- The cluster is controlled by one of the brokers, called the **controller**, which is responsible for administrative actions such as assigning partitions to other brokers and monitoring for failures. The controller is elected from the live members of the cluster.
- A partition can be assigned to more than one broker, in which case the partition is **replicated** across the assigned brokers. This creates redundancy in case one of the brokers fails and allows another broker to take its place without disrupting access to the partition for the users.
- Within a cluster, a single broker owns a partition and is said to be the **leader**. Others called **follower**.
- **Every producer and consumer interacting with the partition must connect to the leader for that partition.**

![](screenshot/09292022_KafkaEducativeio/Screen%20Shot%202022-09-29%20at%205.10.15%20PM.png)

**Producer create messages**

**Consumer read messages and are sometimes known as subscribers or readers.**
- Each partition is read by a single member of the group, though a single consumer can read multiple partitions. 

1 partition, 1 consumer

1 consumer can listen multiple partitions.

![](screenshot/09292022_KafkaEducativeio/Screen%20Shot%202022-09-29%20at%205.15.49%20PM.png)

A **partition rebalance** assigns the partitions that the dead consumer was reading from to the remaining healthy consumers in a consumer group. 

# Kafka Producer

## Producer workflow
![](screenshot/09292022_KafkaEducativeio/Screen%20Shot%202022-09-29%20at%205.34.38%20PM.png)

Producer sending message
- Fire and forget
- Synchronous
- Ascynchronous

# Kafka Consumer

The KafkaConsumer class requires three compulsory properties to be provided: the location of the servers ```bootstrap.servers```, the key deserializer ```key.deserializer```, and the value deserializer ```value.deserializer```


When creating a ```KafkaConsumer``` we can also specify a consumer group id using the property ```group.id```. 


The general pattern for Kafka consumers is to poll for new messages and process them in a perpetual loop, often referred to as the poll loop.

The **offset** identifies the position in a partition up to which a consumer has read. The act of durably storing or updating that position is called the **commit**. 


Code Example for Consumer

```
public class SingleConsumerExample {
    public static void main(String[] args) {
        
        //set up configrations
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        props.put("group.id", "DataJekConsumers");

        // Create a consumer object
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        // Retrieve the partitions for the topic
        List<PartitionInfo> partitionInfos;
        partitionInfos = consumer.partitionsFor("datajek-topic");

        // Select the partitions to consume. In this case, we'll consume all the partitions
        // for the datajek-topic
        Set<TopicPartition> partitionsToConsume = new HashSet<>();
        for (PartitionInfo partition : partitionInfos) {
            partitionsToConsume.add(new TopicPartition(partition.topic(), partition.partition()));
        }

        // Assign the partitions to the consumer
        consumer.assign(partitionsToConsume);
        System.out.println("Consumer assigned itself " + partitionsToConsume.size() + " partitions.");

        try {
            while (true) {
                Duration oneSecond = Duration.ofMillis(1000);
                System.out.println("Consumer polling for data...");
                ConsumerRecords<String, String> records = consumer.poll(oneSecond);
                for (ConsumerRecord<String, String> record : records) {

                    String topic = record.topic();
                    int partition = record.partition();
                    long recordOffset = record.offset();
                    String key = record.key();
                    String value = record.value();

                    System.out.println("\ntopic: " + topic + "\n" +
                            "partition: " + partition + "\n" +
                            "recordOffset: " + recordOffset + "\n" +
                            "key: " + key + "\n" +
                            "value: \n" + value);

                    consumer.commitSync();
                }
            }
        } finally {
            consumer.close();
        }
    }
}
```