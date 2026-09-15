package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Bsnotices;
import com.example.demo.entity.Bsusers;
import com.example.demo.service.BsnoticesService;

@Controller
public class BsnoticesController {

    @Autowired
    private BsnoticesService bsnoticesService;

    // 1. 公告列表頁
    @GetMapping("/brightStaroshirase")
    public String toOshirase(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }
        return "brightStaroshirase"; 
    }

    // 2. 獨立的新增公告表單頁
    @GetMapping("/brightStaroshirase/form")
    public String toOshiraseForm(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }
        return "brightStaroshiraseForm"; 
    }

    // 3. 接收前端送出的「發布新公告」AJAX 請求
    @PostMapping("/api/notices")
    @ResponseBody
    public Map<String, Object> createNotice(@RequestBody Bsnotices notice, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        Bsusers loginUser = (Bsusers) session.getAttribute("loginUser");
        if (loginUser == null) {
            result.put("success", false);
            result.put("message", "請先登入！");
            return result;
        }

        try {
            // 自動帶入當前登入使用者的 ID 作為 publisherId（請確認 Bsusers 有 getId() 方法）
            notice.setPublisherId(loginUser.getId());
            
            bsnoticesService.insertNotice(notice);

            result.put("success", true);
            result.put("message", "發布成功！");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "發布失敗：" + e.getMessage());
        }

        return result;
    }
    @GetMapping("/api/notices")
    @ResponseBody
    public List<Bsnotices> listNotices() {
        return bsnoticesService.getAllNotices();
    }
    
    @DeleteMapping("/api/notices/{id}")
    @ResponseBody
    public Map<String, Object> deleteNotice(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            bsnoticesService.deleteNotice(id); // 呼叫你的 mapper 軟刪除
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }
}