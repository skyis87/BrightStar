package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Bsusers;

@Mapper
public interface BsusersMapper {

    // 動態多條件查詢（傳入帶有 id 的物件即可查詢單筆）
    List<Bsusers> selectSelective(Bsusers row);

    // 選擇性新增（自動處理 is_deleted 預設為 0、created_at 設為 NOW()）
    int insertSelective(Bsusers row);

    // 選擇性修改
    int updateSelective(Bsusers row);

    // 軟刪除（將 is_deleted 設為 1）
    int deleteSelective(Integer id);
}