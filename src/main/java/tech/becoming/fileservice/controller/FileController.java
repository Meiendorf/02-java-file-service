package tech.becoming.fileservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.becoming.fileservice.entity.File;
import tech.becoming.fileservice.helper.FileHelper;
import tech.becoming.fileservice.repository.FileRepository;
import tech.becoming.fileservice.service.FileService;

import java.io.IOException;

@RestController
@RequestMapping("file")
public class FileController {

    private final FileService fileService;

    @Autowired
    private FileHelper fileHelper;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String createFile(@RequestBody File newFile) {

        return fileService.save(newFile).getId();
    }

    @PostMapping(
            value = "{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String uploadFile(@PathVariable String id,
                             @RequestParam("content") MultipartFile fileContent) throws IOException {

        return fileService.setData(id, fileContent.getBytes()).getId();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public File getFile(@PathVariable String id) {
        return fileService.findById(id);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
        final File file = fileService.findById(id);

        return fileHelper.createDownloadResponse(file);
    }



}
