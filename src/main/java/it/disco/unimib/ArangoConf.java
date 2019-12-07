package it.disco.unimib;


import com.arangodb.ArangoDB.Builder;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

;

@Configuration
@EnableArangoRepositories(basePackages = {"it.disco.unimib"})
@Log
public class ArangoConf implements ArangoConfiguration {

	@Value("${arango.host}")
	private String host;
	@Value("${arango.port}")
	private int port;
	@Value("${arango.user:}")
	private String user;
	@Value("${arango.password}")
	private String password;
	@Value("${arango.database}")
	private String database;

	@Override
	public Builder arango() {

		log.info("host: " + host);
		log.info("port: " + port);
		log.info("user: " + user);
		log.info("database: " + database);

		Builder builder = new Builder().host(host, port).user(user).password(password);
		return builder;
	}

	@Override
	public String database() {
		return database;
	}
}