package com.application.api;

import com.domain.Entity.result.OperateResult;
import com.domain.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
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
    public OperateResult downLoadSql(HttpServletRequest request, HttpServletResponse response)throws Exception {
        HttpSession session = request.getSession();
        List<String> sqlList = (List<String>)session.getAttribute("sqlBackUp");
        if (null != sqlList) {
            File sqlFile = dataService.createSqlFile(sqlList.toString());
            if (sqlFile.exists()) {
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition","attachment;fileName=" + sqlFile.getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(sqlFile);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer,0,i);
                        i = bis.read(buffer);
                    }
                    return OperateResult.ok("下载sqlfile成功");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != fis) {
                        fis.close();
                    }
                }
            }
        }
        return OperateResult.error("下载sqlfile失败");
    }
}
