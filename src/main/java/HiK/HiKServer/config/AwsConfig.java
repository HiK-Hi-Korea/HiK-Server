package HiK.HiKServer.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    // IAM 계정의 액세스 키
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    // IAM 계정의 비밀 키
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    // AWS 리전 이름
    @Value("${cloud.aws.region.static}")
    private String region;

    // AmazonS3Client 빈을 생성
    @Bean
    public AmazonS3Client amazonS3Client() {
        // BasicAWSCredentials : accessKey 와 secretKey 를 기반으로 인증 정보를 생성
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()					// AmazonS3ClientBuilder.standard() : S3 클라이언트를 구성하기 위한 빌더 객체를 생성
                .withRegion(region)													// 클라이언트가 작업할 AWS 지역을 설정
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))		// withCredentials() : 액세스 키 및 비밀 키를 제공하는 AWSStaticCredentialsProvider 설정
                .build();															// 이 메서드를 호출하여 최종적으로 AmazonS3Client 인스턴스를 생성하고 반환
    }
}
