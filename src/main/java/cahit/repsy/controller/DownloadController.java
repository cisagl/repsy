package cahit.repsy.controller;

import cahit.repsy.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class DownloadController {

    @Autowired
    private IStorageService storageService;

    @GetMapping("{name}/{version}/{filename}")
    public ResponseEntity<ByteArrayResource> download(
            @PathVariable String name,
            @PathVariable String version,
            @PathVariable String filename
    ) {
        try {
            byte[] fileData = storageService.load(name, version, filename);
            ByteArrayResource resource = new ByteArrayResource(fileData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(fileData.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
