package pc.gear.config.aws;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfiguration {

//    @Bean
//    public S3Client s3Client() {
//        return S3Client.builder().credentialsProvider(awsCredentialsProvider()).build();
//    }
//    @Bean
//    public S3Presigner s3Presigner() {
//        return S3Presigner.builder().credentialsProvider(awsCredentialsProvider()).build();
//    }
//
//    private AwsCredentialsProvider awsCredentialsProvider() {
//        return DefaultCredentialsProvider.builder().build();
//    }

}
