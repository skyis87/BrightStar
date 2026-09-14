package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Bsusers;
import com.example.demo.mapper.BsusersMapper;

/**
 * 使用者業務邏輯服務類別
 */
@Service
public class BsusersService {

    @Autowired
    private BsusersMapper bsusersMapper;

    /**
     * 使用者登入驗證
     * 
     * @param bsusers 前端 AJAX 傳入的 Bsusers 物件
     * @return 驗證成功回傳 Bsusers 物件；若失敗則回傳 null
     */
    public Bsusers login(Bsusers bsusers) {
        if (bsusers == null || bsusers.getUsername() == null || bsusers.getPassword() == null) {
            return null;
        }

        // 建立查詢條件
        Bsusers queryParam = new Bsusers();
        queryParam.setUsername(bsusers.getUsername());
        queryParam.setIsDeleted(0); // 僅查詢未被軟刪除的有效使用者

        // 執行動態查詢
        List<Bsusers> userList = bsusersMapper.selectSelective(queryParam);

        if (userList != null && !userList.isEmpty()) {
            Bsusers dbUser = userList.get(0);
            
            // 比對密碼
            if (dbUser.getPassword() != null && dbUser.getPassword().equals(bsusers.getPassword())) {
                return dbUser;
            }
        }

        return null;
    }
}