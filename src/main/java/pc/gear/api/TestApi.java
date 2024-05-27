package pc.gear.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.dto.excel.ImportProductDto;
import pc.gear.entity.Product;
import pc.gear.repository.ProductRepository;
import pc.gear.repository.custom.ProductCustomRepository;
import pc.gear.request.TestRequest;
import pc.gear.request.product.ProductSearchRequest;
import pc.gear.response.product.ProductSearchResponse;
import pc.gear.service.JwtService;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryDownload;
import software.amazon.awssdk.transfer.s3.model.DirectoryDownload;
import software.amazon.awssdk.transfer.s3.model.DownloadDirectoryRequest;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "test1")
@Log4j2
public class TestApi {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "s3-download-directory")
    public Object s3DownloadDirectory(@RequestBody @Valid TestRequest request) {

        S3AsyncClient s3AsyncClient = S3AsyncClient.crtCreate();
        S3TransferManager transferManager =
                S3TransferManager.builder()
                        .s3Client(s3AsyncClient)
                        .build();
        File file = new File("C:\\Users\\ASUS\\Downloads\\downloads");

        DirectoryDownload directoryDownload = transferManager.downloadDirectory(DownloadDirectoryRequest.builder()
                .destination(Paths.get(file.getAbsolutePath()))
                .listObjectsV2RequestTransformer(l -> l.prefix(request.getFolderName()))
                .filter(obj -> {
                    String fileName = getFileNameFromObjectKey(obj.key());
                    if (StringUtils.isNotBlank(fileName)) {
//                        log.info(fileName + "|" + obj + "|" + obj.key().contains(request.getName()));
                        return obj.key().contains(request.getFileName());
                    }
                    return false;
                })
                .downloadFileRequestTransformer(t -> {
                    String originDest = t.build().getObjectRequest().key();
                    Path newDest = Paths.get(file.getAbsolutePath() + "/" + originDest);
                    System.out.println(originDest + "|" + newDest);
                    t.destination(newDest);
                })
                .bucket("pcgear-my-bucket")
                .build());
        CompletedDirectoryDownload completedDirectoryDownload = directoryDownload.completionFuture().join();

        completedDirectoryDownload.failedTransfers()
                .forEach(fail -> log.warn("Object [{}] failed to transfer", fail.toString()));

        System.out.println(completedDirectoryDownload.failedTransfers().size());
        transferManager.close();
        s3AsyncClient.close();
        return null;
    }

    /**
     * Extracts the file name from an S3 object key.
     *
     * @param objectKey the full object key (path) in the S3 bucket
     * @return the file name extracted from the object key
     */
    public static String getFileNameFromObjectKey(String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            throw new IllegalArgumentException("Object key cannot be null or empty");
        }
        int lastSlashIndex = objectKey.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            // No slash found, the entire key is the file name
            return objectKey;
        }
        // Return the substring after the last slash
        return objectKey.substring(lastSlashIndex + 1);
    }

    @Autowired
    private ProductRepository productRepository;

    @Operation(summary = "My test endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "test")
    public Object test(@RequestParam("pageSize") Integer pageSize, @RequestParam("totalRow") Integer totalRow) {
        ProductSearchRequest searchRequestAll = new ProductSearchRequest();
        searchRequestAll.setPageSize(totalRow);
        searchRequestAll.setPageNumber(1);
        int pageNum = 1;
        List<ProductSearchRequest> lst = new ArrayList<>();
        for (int i = 0; i < totalRow; i += pageSize) {
            ProductSearchRequest searchRequestPart1 = new ProductSearchRequest();
            searchRequestPart1.setPageSize(pageSize);
            searchRequestPart1.setPageNumber(pageNum);
            pageNum++;
            lst.add(searchRequestPart1);
        }

        StopWatch st = new StopWatch();
        st.start();
        ProductSearchResponse searchAll = productRepository.search(searchRequestAll);
        System.out.println("Result: " + searchAll.getContents().size());

        st.stop();
        log.info("Find all once: " + st.getTaskInfo()[0].getTimeMillis());
        st.start();

        lst.parallelStream().map(x -> productRepository.search(x)).forEach(x -> log.info(x.getContents().size()));
        st.stop();
        log.info("Find all twice: " + st.getTaskInfo()[1].getTimeMillis());
        return null;
    }
}
