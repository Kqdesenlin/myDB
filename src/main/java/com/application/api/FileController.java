package com.application.api;

import com.domain.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/21
 * @description:
 */
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200",maxAge = 3600,allowCredentials = "true")
@RequestMapping("/file")
public class FileController {

    @Autowired
    private DataService dataService;

    @GetMapping("/download/sqlfile")
    public ResponseEntity<Resource> downLoadSql(HttpServletRequest request, HttpServletResponse response)throws Exception {
        HttpSession session = request.getSession();
        List<String> sqlList = (List<String>)session.getAttribute("sqlBackUp");
        if (null != sqlList) {
            File sqlFile = dataService.createSqlFile(String.join("",sqlList));
            if (sqlFile.exists()) {
                byte[] isr = Files.readAllBytes(sqlFile.toPath());
                ByteArrayResource resource = new ByteArrayResource(isr);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(new MediaType("text","json"));
                httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + sqlFile.getName());
                return ResponseEntity.ok().headers(httpHeaders).contentLength(sqlFile.length())
                        .body(resource);
//
            }
        }
        return null;
    }

    @GetMapping("/download/datafile")
    public ResponseEntity<Resource> downLoadData(HttpServletRequest request, HttpServletResponse response)throws Exception {
        HttpSession session = request.getSession();
        List<String> sqlList = (List<String>)session.getAttribute("sqlBackUp");
        if (null != sqlList) {
            File sqlFile = new File("tableInfo.txt");
            if (sqlFile.exists()) {
                byte[] isr = Files.readAllBytes(sqlFile.toPath());
                ByteArrayResource resource = new ByteArrayResource(isr);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(new MediaType("text","json"));
                httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + sqlFile.getName());
                return ResponseEntity.ok().headers(httpHeaders).contentLength(sqlFile.length())
                        .body(resource);
//
            }
        }
        return null;
    }
}
