package org.imdb.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    /**
     * Creates a connection to elasticsearch on port 9200
     * @return ElasticsearchClient
     */
    @Bean
    public ElasticsearchClient getElasticClient(){
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200),
                new HttpHost("elasticsearch", 9200)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);
        return client;
    }

    @Bean
    public RestClient getRestClient(){
        return RestClient.builder(new HttpHost("localhost", 9200)).build();
    }
}
