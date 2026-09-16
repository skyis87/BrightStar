package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

    // 3. 執行更新操作
    @PutMapping("/api/notices/{id}")
    @ResponseBody
    public Map<String, Object> updateNotice(@PathVariable Integer id, @RequestBody Bsnotices notice) {
        Map<String, Object> result = new HashMap<>();
        try {
            notice.setId(id);
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