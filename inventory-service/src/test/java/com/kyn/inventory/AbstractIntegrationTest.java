package com.kyn.inventory;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(properties = {
	"logging.level.root=ERROR",
	"logging.level.com.kyn*=INFO",
	"spring.cloud.stream.kafka.binder.configuration.auto.offset.reset=earliest",
	"spring.datasource.url=r2dbc:postgresql://localhost:5432/kynpost",
	"spring.datasource.username=kynpostgres",
	"spring.datasource.password=kynpostgrespw",
	"spring.data.redis.host=localhost",
	"spring.data.redis.port=6379",
	"spring.data.redis.timeout=2000ms",
	"spring.data.redis.connect-timeout=2000ms",
	"spring.sql.init.mode=always",
	"spring.sql.init.schema-locations=classpath:schema.sql"
})
@EmbeddedKafka(
		partitions = 1,
		bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
public class AbstractIntegrationTest {
    
}
