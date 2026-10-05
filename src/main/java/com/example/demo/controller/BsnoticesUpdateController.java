package com.example.demo.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Bsnotices;
import com.example.demo.service.BsnoticesService;

@Controller
public class BsnoticesUpdateController {

    @Autowired
    private BsnoticesService bsnoticesService;

    // 1. 導向「編輯公告」表單頁
    @GetMapping("/brightStaroshirase/edit")
    public String toOshiraseEdit(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }
        return "brightStaroshiraseEdit"; 
    }

    // 2. 取得單筆公告資料 (供編輯表單回填用)
    @GetMapping("/api/notices/{id}/edit")
    @ResponseBody
    public Bsnotices getNoticeForEdit(@PathVariable Integer id) {
        return bsnoticesService.getNoticeById(id);
    }

    // 3. 執行更新操作 (支援 FormData、檔案上傳與絕對路徑儲存)
    @PutMapping("/api/notices/{id}")
    @ResponseBody
    public Map<String, Object> updateNotice(
            @PathVariable Integer id,
            @ModelAttribute Bsnotices notice,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            notice.setId(id);
            
            // 取得資料庫原本的資料，用來保留舊的檔案資訊
            Bsnotices oldNotice = bsnoticesService.getNoticeById(id);
            if (oldNotice != null) {
                notice.setFileName(oldNotice.getFileName());
                notice.setFilePath(oldNotice.getFilePath());
            }
            
            // 如果使用者有另外選擇了新檔案，才執行儲存與覆蓋邏輯
            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                
                // 統一使用與新增相同的高穩定性絕對路徑寫法
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs(); // 若資料夾不存在則強制建立
                }
                
                // 避免檔名重複，加上時間戳記
                String savedFileName = System.currentTimeMillis() + "_" + originalFilename;
                File dest = new File(uploadDir + savedFileName);
                
                // 將檔案實際寫入硬碟
                file.transferTo(dest);
                
                // 更新為新檔案的資訊
                notice.setFileName(originalFilename);
                notice.setFilePath("/uploads/" + savedFileName);
            }

            // 執行資料庫更新
            bsnoticesService.updateNotice(notice);
            
            result.put("success", true);
            result.put("message", "更新成功！");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "更新失敗：" + e.getMessage());
        }
        return result;
    }
}