package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Bsnotices;

@Mapper
public interface BsnoticesMapper {

    // 動態多條件查詢（若物件內只傳入 id，即可查出單筆）
    List<Bsnotices> selectSelective(Bsnotices row);

    // 選擇性新增（自動處理 is_deleted、created_at、updated_at）
    int insertSelective(Bsnotices row);

    // 選擇性修改（自動更新 updated_at）
    int updateSelective(Bsnotices row);

    // 軟刪除（將 is_deleted 設為 1）
    int deleteSelective(Integer id);
}