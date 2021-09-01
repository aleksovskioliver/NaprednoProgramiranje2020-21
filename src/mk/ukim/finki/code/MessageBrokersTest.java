package mk.ukim.finki.code;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PartitionAssigner {
    public static Integer assignPartition (Message message, int partitionsCount) {
        return (Math.abs(message.key.hashCode())  % partitionsCount) + 1;
    }
}
class PartitionDoesNotExistException extends Exception{
    public PartitionDoesNotExistException(String topic,Integer partitionCount) {
        super(String.format("The topic %s does not have a partition with number %d",topic,partitionCount));
    }
}
class Message implements Comparable<Message>{
    LocalDateTime timestamp;
    String message;
    Integer partition;
    String key;

    public Message(LocalDateTime timestamp, String message, Integer partition, String key) {
        this.timestamp = timestamp;
        this.message = message;
        this.partition = partition;
        this.key = key;
    }
    public Message(LocalDateTime timestamp, String message, String key) {
        this.timestamp = timestamp;
        this.message = message;
        this.key = key;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Integer getPartition() {
        return partition;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Message{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
    @Override
    public int compareTo(Message o) {
        return this.timestamp.compareTo(o.timestamp);
    }
}
class Topic{
    String topicName;
    int partitionsCount;
    //Map<partition,TreeSet<Message>>
    Map<Integer,TreeSet<Message>> messagesByPartition;

    public Topic(String topicName,int partitionsCount){
        this.topicName = topicName;
        this.partitionsCount = partitionsCount;
        this.messagesByPartition = new TreeMap<>();
        IntStream.range(1,partitionsCount+1)
                .forEach(i->this.messagesByPartition.put(i,new TreeSet<>()));
    }
    void addMessage(Message message) throws PartitionDoesNotExistException {
        Integer partition = message.partition;
        if (partition==null)
            partition = (Math.abs(message.key.hashCode())  % partitionsCount) + 1;
        if (!messagesByPartition.containsKey(partition))
            //The topic topic1 does not have a partition with number 7
            throw new PartitionDoesNotExistException(topicName,partition);

        this.messagesByPartition.computeIfPresent(partition,(k,v)->{
            if (v.size() == MessageBroker.capacityPerTopic)
                v.remove(v.first());
            v.add(message);
            return v;
        });
    }
    void changeNumberOfPartitions (int newPartitionsNumber) throws UnsupportedOperationException {
        if (newPartitionsNumber < partitionsCount)
            throw new UnsupportedOperationException("Partitions number cannot be decreased!");

        int difference = newPartitionsNumber - this.partitionsCount;
        int size = this.messagesByPartition.size();
        IntStream.range(1,difference+1)
                .forEach(i->this.messagesByPartition.putIfAbsent(size+i,new TreeSet<>()));
        partitionsCount = newPartitionsNumber;
    }

    @Override
    public String toString() {
        return String.format("Topic: %10s Partitions: %5d\n%s",
                this.topicName,
                this.partitionsCount,
                this.messagesByPartition.entrySet()
                        .stream()
                        .map(entry -> String.format("%2d : Count of messages: %5d\n%s",
                                entry.getKey(),
                                entry.getValue().size(),
                                !entry.getValue().isEmpty() ? "Messages:\n" +
                                        entry.getValue().stream().map(Message::toString)
                                                .collect(Collectors.joining("\n"))
                                        : "")).collect(Collectors.joining("\n")));
    }
}
class MessageBroker{
    static LocalDateTime minimumDate;
    static Integer capacityPerTopic;
    //Map<topicName,Topic>
    Map<String,Topic> topicsByName;

    public MessageBroker(LocalDateTime minimumDate,Integer capacityPerTopic){
        MessageBroker.minimumDate = minimumDate;
        MessageBroker.capacityPerTopic = capacityPerTopic;
        this.topicsByName = new TreeMap<>();
    }
    void addTopic(String topic,int partitionsCount){
        this.topicsByName.put(topic,new Topic(topic,partitionsCount));
    }
    public void addMessage(String topic,Message message) throws PartitionDoesNotExistException {
        if (message.getTimestamp().isBefore(minimumDate))
            return;
        this.topicsByName.get(topic).addMessage(message);
    }
    void changeTopicSettings (String topic, int partitionsCount) throws UnsupportedOperationException {
        this.topicsByName.get(topic).changeNumberOfPartitions(partitionsCount);
    }

    //Broker with  1 topics:
    @Override
    public String toString() {
        return String.format("Broker with %d topics:\n%s",
                topicsByName.size(),
                topicsByName.values()
                    .stream()
                        .map(Topic::toString)
                        .collect(Collectors.joining("\n")));
    }
}
class UnsupportedOperationException extends Exception{
    public UnsupportedOperationException(String message) {
        super(message);
    }
}


public class MessageBrokersTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String date = sc.nextLine();
        LocalDateTime localDateTime =LocalDateTime.parse(date);
        Integer partitionsLimit = Integer.parseInt(sc.nextLine());
        MessageBroker broker = new MessageBroker(localDateTime, partitionsLimit);
        int topicsCount = Integer.parseInt(sc.nextLine());

        //Adding topics
        for (int i=0;i<topicsCount;i++) {
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topicName = parts[0];
            int partitionsCount = Integer.parseInt(parts[1]);
            broker.addTopic(topicName, partitionsCount);
        }

        //Reading messages
        int messagesCount = Integer.parseInt(sc.nextLine());

        System.out.println("===ADDING MESSAGES TO TOPICS===");
        for (int i=0;i<messagesCount;i++) {
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topic = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
            String message = parts[2];
            if (parts.length==4) {
                String key = parts[3];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,key));
                } catch (PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                Integer partition = Integer.parseInt(parts[3]);
                String key = parts[4];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,partition,key));
                } catch (PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("===BROKER STATE AFTER ADDITION OF MESSAGES===");
        System.out.println(broker);

        System.out.println("===CHANGE OF TOPICS CONFIGURATION===");
        //topics changes
        int changesCount = Integer.parseInt(sc.nextLine());
        for (int i=0;i<changesCount;i++){
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topicName = parts[0];
            Integer partitions = Integer.parseInt(parts[1]);
            try {
                broker.changeTopicSettings(topicName, partitions);
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("===ADDING NEW MESSAGES TO TOPICS===");
        messagesCount = Integer.parseInt(sc.nextLine());
        for (int i=0;i<messagesCount;i++) {
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topic = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
            String message = parts[2];
            if (parts.length==4) {
                String key = parts[3];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,key));
                } catch (PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                Integer partition = Integer.parseInt(parts[3]);
                String key = parts[4];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,partition,key));
                } catch (PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("===BROKER STATE AFTER CONFIGURATION CHANGE===");
        System.out.println(broker);


    }
}
