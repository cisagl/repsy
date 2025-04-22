package cahit.repsy.storage;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    void save(String packageName, String version, MultipartFile file, String fileName) throws Exception;
    byte[] load(String packageName, String version, String fileName) throws Exception;
}
