package com.kyn.message;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(properties = {
		"logging.level.root=ERROR",
		"logging.level.com.kyn*=INFO",
		"spring.cloud.stream.kafka.binder.configuration.auto.offset.reset=earliest",
        "spring.data.mongodb.uri=mongodb://localhost:27017/message"
})
@EmbeddedKafka(
		partitions = 1,
		bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
public abstract class AbstractIntegrationTest {


}