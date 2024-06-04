package pc.gear.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class S3ServiceImpl {

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;


    /**
     * Check if object is existed in s3.
     *
     * @param bucketName String
     * @param key String
     * @return boolean
     */
    public boolean exists(String bucketName, String key) {
        HeadObjectRequest request = HeadObjectRequest.builder().bucket(bucketName).key(key).build();
        try {
            s3Client.headObject(request);
            return true;
        } catch (NoSuchKeyException ex) {
            return false;
        }
    }

    /**
     *
     * @param bucketName
     * @param key
     * @param file
     * @return
     */
    public String uploadFileToS3(String bucketName, String key, File file) {
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentLength(file.length())
                        .contentType("application/octet-stream")
                        .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
        GetUrlRequest getUrlRequest = GetUrlRequest.builder().bucket(bucketName).key(key).build();
        return s3Client.utilities().getUrl(getUrlRequest).toExternalForm();
    }

    public String uploadInputStreamToS3(String bucketName, String key, InputStream inputStream) throws Exception {
        CreateMultipartUploadRequest createMultipartUploadRequest =
                CreateMultipartUploadRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType("application/octet-stream")
                        .build();
        CreateMultipartUploadResponse multipartUpload = s3Client.createMultipartUpload(createMultipartUploadRequest);
        String uploadId = multipartUpload.uploadId();
        List<CompletedPart> completedParts = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int s3MultipartSize = 5 * 1024 * 1024;
        while (inputStream.available() > 0) {
            byte[] bytes = new byte[s3MultipartSize];
            long length = inputStream.readNBytes(bytes, 0, s3MultipartSize);
            int partNo = atomicInteger.incrementAndGet();

            CompletableFuture.runAsync(() -> {
                UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .uploadId(uploadId)
                        .partNumber(partNo)
                        .contentLength(length)
                        .build();
                UploadPartResponse uploadPartResponse = s3Client.uploadPart(uploadPartRequest, RequestBody.fromBytes(bytes));
                completedParts.add(CompletedPart.builder().partNumber(partNo).eTag(uploadPartResponse.eTag()).build());
            });
        }
        inputStream.close();

        int waitTime = 0;
        int maxAwaitTime = 30 * 1000;
        while (completedParts.size() < atomicInteger.get()) {
            Thread.sleep(200);
            waitTime += 200;
            if (waitTime > maxAwaitTime) {
                throw new TimeoutException();
            }
        }
        completedParts.sort(Comparator.comparingInt(CompletedPart::partNumber));

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                CompleteMultipartUploadRequest.builder().bucket(bucketName).key(key)
                        .uploadId(uploadId)
                        .multipartUpload(c -> c.parts(completedParts))
                        .build();
        s3Client.completeMultipartUpload(completeMultipartUploadRequest);

        GetUrlRequest getUrlRequest = GetUrlRequest.builder().bucket(bucketName).key(key).build();
        return s3Client.utilities().getUrl(getUrlRequest).toExternalForm();
    }

    public OutputStream downloadFile2OutputStream(String bucketName, String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);
        OutputStream outputStream = new ByteArrayOutputStream();
        response.transferTo(outputStream);
        return outputStream;
    }

    public InputStream downloadFile2InputStream(String bucketName, String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(getObjectRequest);
        return response != null ? response.asInputStream() : null;
    }

    public void downloadFile2LocalDir(String bucketName, String key, String pathToFile) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(getObjectRequest);
        byte[] bytes = response.asByteArray();
        File file = new File(pathToFile);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(bytes);
        }
    }


    public URL generatePresignUrl(String bucketName, String key, HttpMethod httpMethod, long expireTime) {
        if (httpMethod.matches(HttpMethod.PUT.name())) {
            if (!exists(bucketName, key)) {
                return null;
            }
            PutObjectPresignRequest putObjectPresignRequest =
                    PutObjectPresignRequest.builder()
                            .putObjectRequest(putObjectRequest -> putObjectRequest.bucket(bucketName).key(key))
                            .signatureDuration(Duration.ofHours(expireTime))
                            .build();

            PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(putObjectPresignRequest);
            return presignedPutObjectRequest != null ? presignedPutObjectRequest.url() : null;

        } else if (httpMethod.matches(HttpMethod.GET.name())) {
            if (!exists(bucketName, key)) {
                return null;
            }
            GetObjectPresignRequest getObjectPresignRequest =
                    GetObjectPresignRequest.builder()
                            .getObjectRequest(getObjectRequest -> getObjectRequest.bucket(bucketName).key(key))
                            .signatureDuration(Duration.ofHours(expireTime))
                            .build();

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
            return presignedGetObjectRequest != null ? presignedGetObjectRequest.url() : null;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean copyS3Object(String sourceBucket, String sourceKey, String destBucket, String destKey) {
        CopyObjectRequest copyObjectRequest =
                CopyObjectRequest.builder()
                        .sourceBucket(sourceBucket)
                        .sourceKey(sourceKey)
                        .destinationBucket(destBucket)
                        .destinationKey(destKey)
                        .build();
        return s3Client.copyObject(copyObjectRequest) != null;
    }

    public boolean renameS3object(String bucketName, String sourceKey, String destKey) {
        boolean response = copyS3Object(bucketName, sourceKey, bucketName, destKey);
        deleteS3Object(bucketName, sourceKey);
        return response;
    }

    public boolean deleteS3Object(String bucket, String key) {
        DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();
        return s3Client.deleteObject(deleteObjectRequest) != null;
    }

}
