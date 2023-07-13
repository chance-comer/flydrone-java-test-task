package ru.flydrone.flydronejavatesttask;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.context.annotation.Bean;

public class AmazonS3Client {
    @Bean(name = "amazonS3Client")
    public AmazonS3 amazonS3Client(AWSCredentialsProvider credentialsProvider,
                                   RegionProvider regionProvider,
                                   @Value("${aws.s3.default-endpoint:https://s3.amazonaws.com}") String endpoint) {

        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpoint, regionProvider.getRegion().getName()))
                .build();
    }
}
