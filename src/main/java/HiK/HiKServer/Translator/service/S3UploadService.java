package HiK.HiKServer.Translator.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3UploadService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException{
        String originalFilename = multipartFile.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public String uploadByteStringToS3(ByteString audioContents, String fileName) {
        String bucketName = bucket;  // Your bucket name

        // Convert ByteString to InputStream
        InputStream inputStream = new ByteArrayInputStream(audioContents.toByteArray());

        // Create metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("audio/mpeg");
        metadata.setContentLength(audioContents.size());

        // Create a PutObjectRequest
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata);

        // Upload to S3
        PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

        // Return object URL
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

}
