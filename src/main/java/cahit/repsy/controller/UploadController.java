package cahit.repsy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cahit.repsy.model.PackageInfo;
import cahit.repsy.entity.PackageEntity;
import cahit.repsy.repo.PackageRepository;
import cahit.repsy.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private IStorageService storageService;
    @Autowired
    private PackageRepository packageRepository;

    @PostMapping("/{name}/{version}")
    public ResponseEntity<String> uploadPackage(
            @PathVariable String name,
            @PathVariable String version,
            @RequestParam("package.rep") MultipartFile packageFile,
            @RequestParam("meta.json") MultipartFile metaFile
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PackageInfo meta = mapper.readValue(metaFile.getBytes(), PackageInfo.class);

            if (!meta.getName().equals(name) || !meta.getVersion().equals(version)) {
                String errorMessage = "meta.json içeriği path ile uyuşmuyor.";
                logger.warn(errorMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }

            storageService.save(name, version, packageFile, "package.rep");
            storageService.save(name, version, metaFile, "meta.json");

            PackageEntity entity = new PackageEntity();
            entity.setName(name);
            entity.setVersion(version);
            entity.setAuthor(meta.getAuthor());
            packageRepository.save(entity);

            logger.info("Paket başarıyla yüklendi: {} - {}", name, version);
            return ResponseEntity.ok("Yükleme başarılı");

        } catch (IOException e) {
            String errorMessage = "meta.json okunamadı: " + e.getMessage();
            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Hata oluştu: " + e.getMessage();
            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}