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

    // 2. 新增公告表單頁 (改導向 brightStaroshiraseCreate)
    @GetMapping("/brightStaroshirase/form")
    public String toOshiraseCreate(HttpSession session) {
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
    public List<Bsnotices> listNotices(HttpSession session) {
        Bsusers loginUser = (Bsusers) session.getAttribute("loginUser");
        
        String role = (loginUser != null && loginUser.getRole() != null) 
                    ? loginUser.getRole() 
                    : "";

        return bsnoticesService.getNoticesByRole(role);
    }
    
    @DeleteMapping("/api/notices/{id}")
    @ResponseBody
    public Map<String, Object> deleteNotice(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            bsnoticesService.deleteNotice(id);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }
    
 // 4. 導向「公告詳細內容」頁面
    @GetMapping("/brightStaroshirase/detail")
    public String toOshiraseDetail(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }
        return "brightStaroshiraseDetail";
    }

    // 5. 取得單筆公告詳細資料 API (供詳細頁 AJAX 呼叫)
    @GetMapping("/api/notices/{id}")
    @ResponseBody
    public Bsnotices getNoticeDetail(@PathVariable Integer id) {
        return bsnoticesService.getNoticeById(id);
    }
}