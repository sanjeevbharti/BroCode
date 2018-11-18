package com.epam.hackathon.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:data.properties")
public class MongoDBConfig {

    @Value("${mongo.db.uri}")
    private String mongoDbUri;
    @Value("${mongo.db.database}")
    private String mongoDatabase;

    @Bean(name = "dataSource")
    public Datastore datastore() {
        MongoClientURI uri = new MongoClientURI(mongoDbUri);

        MongoClient mongoClient = new MongoClient(uri);
        Morphia morphia = new Morphia();
        Datastore dataStore = morphia.createDatastore(mongoClient, mongoDatabase);
        return dataStore;
    }
}
