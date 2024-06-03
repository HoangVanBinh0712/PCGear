package pc.gear.util;

import lombok.experimental.UtilityClass;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@UtilityClass
public class FileUtils {


    public static String moveFile(String sourcePath, String destinationPath) throws IOException {
        return Files.move(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING).toString();
    }

    public static String copyFile(String sourcePath, String destinationPath) throws IOException {
        return Files.copy(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING).toString();
    }

    public static ByteArrayOutputStream zipFile(String sourceFile) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(bos);

        File fileToZip = new File(sourceFile);
        int bufferSize = (int) fileToZip.length();

        ZipEntry zipEntry = new ZipEntry((fileToZip.getName()));
        zipOutputStream.putNextEntry(zipEntry);

        try (FileInputStream fileInputStream = new FileInputStream(fileToZip)) {
            byte[] bytes = new byte[bufferSize + 1];
            int len;
            while ((len = fileInputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, len);
            }
        }
        zipOutputStream.close();
        bos.close();
        return bos;
    }

    public static File zipFileOverSize(String sourceFile, long maxSize) throws IOException {
        File fileToZip = new File(sourceFile);
        if (fileToZip.length() < maxSize) {
            return fileToZip;
        }
        String outputPath;
        if (sourceFile.contains(".")) {
            outputPath = sourceFile.substring(0, sourceFile.lastIndexOf(".")).concat(".zip");
        } else {
            outputPath = sourceFile.concat(".zip");
        }
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(outputPath);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fos)) {
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOutputStream.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fis.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, len);
            }
            return new File(outputPath);
        }

    }

    public static ByteArrayOutputStream zipDir(String pathToDir) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        File dirToZip = new File(pathToDir);
        zipChildrenFile(dirToZip, dirToZip.getName(), zipOutputStream);
        zipOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream;
    }

    public static void zipChildrenFile(File fileToZip, String fileName, ZipOutputStream zipOutputStream) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            ZipEntry zipEntry = new ZipEntry(fileName + '/');
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.closeEntry();
            // get child file
            File[] files = fileToZip.listFiles();
            if (files != null) {
                for (File file : files) {
                    zipChildrenFile(file, fileName + '/' + file.getName(), zipOutputStream);
                }
            }
            return;
        }
        int bufferSize = (int) fileToZip.length();
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);
        byte[] bytes = new byte[bufferSize + 1];
        int len;

        try (FileInputStream fileInputStream = new FileInputStream(fileToZip)) {
            while ((len = fileInputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, len);
            }
        }
    }

    public static boolean unzipFile(String pathToZip, String destPath) throws IOException {
        File desDir = new File(destPath);
        if (!desDir.exists()) {
            desDir.mkdir();
        }
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(pathToZip))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String filePath = destPath + File.separator + zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    File dir = new File(filePath);
                    dir.mkdirs();
                } else {
                    extractFile(zipInputStream, filePath);
                }

                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }
        }
        return true;
    }

    public static void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            File file = new File(filePath);
            int bufferSize = (int) file.length();
            byte[] bytes = new byte[bufferSize + 1];
            int read;
            while ((read = zipInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, read);
            }
        }
    }

    public static String createFile(String path, byte[] content) throws IOException {
        Path filePath = Path.of(path);
        Path parentPath = filePath.getParent();
        if (parentPath != null) {
            Files.createDirectories(parentPath);
        }
        Files.write(filePath, content);
        return filePath.toString();
    }

    public static boolean deleteFiles(String pathToFile) throws IOException {
        File file = new File(pathToFile);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    if (childFile.isDirectory()) {
                        deleteFiles(childFile.getPath());
                    } else {
                        deleteFile(childFile.getPath());
                    }
                }
            }
        }
        return Files.deleteIfExists(Paths.get(file.getPath()));
    }

    public static void deleteFile(String pathToFile) throws IOException {
        Path path = Paths.get(pathToFile);
        if (path.toFile().exists()) {
            Files.delete(path);
        }
    }

}
