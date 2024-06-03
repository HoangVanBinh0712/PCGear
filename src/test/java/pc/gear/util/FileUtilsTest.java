package pc.gear.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtilsTest {

    @Test
    public void testMoveFile() throws IOException {

        String filePath = "src/test/java/pc/gear/resources/temp/folder1/text.txt";
        String destPath = "src/test/java/pc/gear/resources/temp/folder2/text.txt";
        try {
            // Create a File object
            FileUtils.createFile(filePath, "helloworld".getBytes(StandardCharsets.UTF_8));
            File file = new File(FileUtils.moveFile(filePath, destPath));
            Assertions.assertTrue(file.exists());
        } finally {
            // delete file after all
            FileUtils.deleteFile(destPath);
        }
    }

    @Test
    public void testCopyFile() throws IOException {

        String filePath = "src/test/java/pc/gear/resources/temp/folder1/text.txt";
        String destPath = "src/test/java/pc/gear/resources/temp/folder2/text.txt";
        try {
            // Create a File object
            FileUtils.createFile(filePath, "helloworld".getBytes(StandardCharsets.UTF_8));
            File file = new File(FileUtils.copyFile(filePath, destPath));
            Assertions.assertTrue(file.exists());
        } finally {
            // delete file after all
            FileUtils.deleteFile(filePath);
            FileUtils.deleteFile(destPath);
        }
    }

    @Test
    public void testZipFile() throws IOException {

        String path = "src/test/java/pc/gear/resources/temp/folder1";
        String file1 = path + "/text.txt";
        File resultFile = null;
        try {
            // Create a File object
            FileUtils.createFile(file1, "helloworld1".getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream bos = FileUtils.zipFile(file1);
            String resultPath = FileUtils.createFile(path + "/result.zip", bos.toByteArray());
            resultFile = new File(resultPath);
            Assertions.assertTrue(resultFile.exists());
        } finally {
            // delete file after all
            if (resultFile != null) {
                FileUtils.deleteFile(resultFile.getPath());
            }
        }
    }

    @Test
    public void testZipFileOverSize1() throws IOException {

        String path = "src/test/java/pc/gear/resources/temp/folder1";
        String file1 = path + "/text.txt";
        File resultFile = null;
        try {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                s.append("helloworld1 ");
            }
            // Create a File object
            FileUtils.createFile(file1, s.toString().getBytes(StandardCharsets.UTF_8));
            resultFile = FileUtils.zipFileOverSize(file1, 100);
            Assertions.assertTrue(resultFile.exists());
        } finally {
            // delete file after all
            if (resultFile != null) {
                FileUtils.deleteFile(resultFile.getPath());
            }
        }
    }

    @Test
    public void testZipFileOverSize2() throws IOException {

        String path = "src/test/java/pc/gear/resources/temp/folder1";
        String file1 = path + "/text.txt";
        File resultFile = null;
        try {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                s.append("helloworld1 ");
            }
            // Create a File object
            FileUtils.createFile(file1, s.toString().getBytes(StandardCharsets.UTF_8));
            resultFile = FileUtils.zipFileOverSize(file1, 100000);
            Assertions.assertTrue(resultFile.exists());
        } finally {
            // delete file after all
            if (resultFile != null) {
                FileUtils.deleteFile(resultFile.getPath());
            }
        }
    }

    @Test
    public void testZipDir() throws IOException {

        String path = "src/test/java/pc/gear/resources/temp/folder1";
        String file1 = path + "/text.txt";
        String file2 = path + "/sub/sub1.txt";
        File resultFile = null;
        try {
            // Create a File object
            FileUtils.createFile(file1, "helloworld1".getBytes(StandardCharsets.UTF_8));
            FileUtils.createFile(file2, "helloworld2".getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream bos = FileUtils.zipDir(path);
            String resultPath = FileUtils.createFile(path + "/result.zip", bos.toByteArray());
            resultFile = new File(resultPath);
            Assertions.assertTrue(resultFile.exists());
        } finally {
            // delete file after all
            if (resultFile != null) {
                FileUtils.deleteFile(resultFile.getPath());
            }
        }
    }

    @Test
    public void testDeleteFiles() throws IOException {

        String path = "src/test/java/pc/gear/resources/temp/folder1";
        String file2 = path + "/sub/sub1.txt";
        // Create a File object
        FileUtils.createFile(file2, "helloworld2".getBytes(StandardCharsets.UTF_8));

        Assertions.assertTrue(FileUtils.deleteFiles(path + "/sub"));
    }

    @Test
    public void testUnzipFile() throws IOException {

        String path = "src/test/java/pc/gear/resources/temp/folder1";
        String file1 = path + "/text.txt";
        String file2 = path + "/sub/sub1.txt";
        File resultFile = null;
        try {
            // Create a File object
            FileUtils.createFile(file1, "helloworld1".getBytes(StandardCharsets.UTF_8));
            FileUtils.createFile(file2, "helloworld2".getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream bos = FileUtils.zipDir(path);
            String resultPath = FileUtils.createFile(path + "/result.zip", bos.toByteArray());
            resultFile = new File(resultPath);
            // now unzip the file
            Assertions.assertTrue(FileUtils.unzipFile(resultPath, path + "/result"));
        } finally {
            // delete file after all
            if (resultFile != null) {
                FileUtils.deleteFile(resultFile.getPath());
                FileUtils.deleteFiles(path + "/result");
            }
        }
    }
}
