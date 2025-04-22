package cahit.repsy.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
@ConditionalOnProperty(name = "storage.strategy", havingValue = "file-system", matchIfMissing = true)
public class FileSystemStorage implements IStorageService {

    private final Path rootDir;

    public FileSystemStorage(@Value("${file.storage.path:uploads}") String baseDir) {
        this.rootDir = new File(baseDir).getAbsoluteFile().toPath();
    }

    @Override
    public void save(String packageName, String version, MultipartFile file, String fileName) throws IOException {
        Path folderPath = rootDir.resolve(Paths.get(packageName, version));
        Files.createDirectories(folderPath);
        Path filePath = folderPath.resolve(fileName);
        file.transferTo(filePath.toFile());
    }

    @Override
    public byte[] load(String packageName, String version, String fileName) throws IOException {
        Path filePath = rootDir.resolve(Paths.get(packageName, version, fileName));
        return Files.readAllBytes(filePath);
    }
}
