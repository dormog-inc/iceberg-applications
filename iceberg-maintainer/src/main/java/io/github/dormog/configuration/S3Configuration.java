package io.github.dormog.configuration;

import io.github.dormog.configuration.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@RequiredArgsConstructor
public class S3Configuration {
    private final S3Properties s3Properties;
    private String warehousePath;
    private String s3Endpoint;
    private S3Client s3;

    @PostConstruct
    public void initializeS3Client() throws URISyntaxException {
        warehousePath = "s3a://" + s3Properties.getBucket() + "/my-iceberg-warehouse";
        s3Endpoint = s3Properties.getUrl() + ":" + s3Properties.getPort();
        DefaultCredentialsProvider credentialsProvider = DefaultCredentialsProvider.builder().build();
        Region region = Region.US_EAST_1;
        s3 = S3Client.builder()
                .region(region)
                .endpointOverride(new URI(s3Endpoint))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    public String getWarehousePath() {
        return "s3a://" + s3Properties.getBucket() + "/my-iceberg-warehouse";
    }

    public String getEndpoint() {
        return s3Properties.getUrl() + ":" + s3Properties.getPort();
    }

    public void createBucket(String bucketName) {
        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();
        s3.createBucket(bucketRequest);
    }
}
