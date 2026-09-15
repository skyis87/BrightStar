package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

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
    
    public List<Bsnotices> getAllNotices() {
        // 傳入 null 代表不設條件，撈出所有 is_deleted = 0 的公告
        return bsnoticesMapper.selectSelective(null);
    }

    public int deleteNotice(Integer id) {
        return bsnoticesMapper.deleteSelective(id);
    }
}
