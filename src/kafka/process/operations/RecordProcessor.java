package kafka.process.operations;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface RecordProcessor {
	void onReceive(ConsumerRecord<String, String> r);
}