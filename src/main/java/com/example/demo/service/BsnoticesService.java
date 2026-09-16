package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Bsnotices;
import com.example.demo.mapper.BsnoticesMapper; // 請確認你的 Mapper 介面名稱是否為 BsnoticesMapper

@Service
public class BsnoticesService {

    @Autowired
    private BsnoticesMapper bsnoticesMapper;

    public int insertNotice(Bsnotices notice) {
        // 設定預設值
        if (notice.getCreatedAt() == null) {
            notice.setCreatedAt(LocalDateTime.now());
        }
        if (notice.getUpdatedAt() == null) {
            notice.setUpdatedAt(LocalDateTime.now());
        }
        if (notice.getIsDeleted() == null) {
            notice.setIsDeleted(0); // 0代表未刪除
        }
        return bsnoticesMapper.insertSelective(notice); 
    }
    
 // 依角色過濾
    public List<Bsnotices> getNoticesByRole(String role) {
        boolean isAdminGroup = "HR".equalsIgnoreCase(role) || "GA".equalsIgnoreCase(role);

        Map<String, Object> params = new HashMap<>();
        // 如果不是 HR 或 GA（也就是一般員工），才需要過濾未來公告
        if (!isAdminGroup) {
            params.put("filterFuture", true);
        }

        return bsnoticesMapper.selectSelective(params);
    }
    
    public List<Bsnotices> getAllNotices() {
        // 傳入 null 代表不設條件，撈出所有 is_deleted = 0 的公告
        return bsnoticesMapper.selectSelective(null);
    }

    public int deleteNotice(Integer id) {
        return bsnoticesMapper.deleteSelective(id);
    }
    
 // 依 ID 取得單筆公告
    public Bsnotices getNoticeById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Bsnotices> list = bsnoticesMapper.selectSelective(params);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    // 更新公告
    public int updateNotice(Bsnotices notice) {
        return bsnoticesMapper.updateSelective(notice);
    }
}
