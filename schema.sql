-- 切換使用 learning 資料庫
USE learning;

-- --------------------------------------------------------
-- 1. 用戶表 (users)
-- 包含總務、人事與普通人三種權限
-- --------------------------------------------------------


CREATE TABLE BSUsers (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用戶唯一ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登入帳號',
    password VARCHAR(255) NOT NULL COMMENT '登入密碼',
    real_name VARCHAR(50) NOT NULL COMMENT '使用者姓名',
    role VARCHAR(20) NOT NULL COMMENT '角色權限：GA(總務), HR(人事), EMPLOYEE(普通人)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '帳號建立時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用戶表';

-- --------------------------------------------------------
-- 2. 發布內容表 (notices)
-- 動態管理系統：包含大標題、小標題、年月日、文檔內容與附件下載路徑
-- --------------------------------------------------------
CREATE TABLE BSNotices (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '公告唯一ID',
    main_title VARCHAR(150) NOT NULL COMMENT '大標題',
    sub_title VARCHAR(150) COMMENT '小標題',
    content TEXT NOT NULL COMMENT '文檔內容 / 公告內文',
    category VARCHAR(20) NOT NULL COMMENT '發布分類：GA(總務通知), HR(人事通知)',
    file_path VARCHAR(255) COMMENT '可下載文檔的伺服器儲存路徑',
    file_name VARCHAR(150) COMMENT '下載時顯示的原檔案名稱',
    publisher_id INT NOT NULL COMMENT '發布人ID (關聯 users.id)',
    publish_date DATE NOT NULL COMMENT '發布年月日',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最後更新時間',
    FOREIGN KEY (publisher_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='會社通知動態管理表';

-- --------------------------------------------------------
-- 預設測試資料 (Pre-inserted Test Data)
-- --------------------------------------------------------

-- 插入三種角色的預設用戶
INSERT INTO BSUsers (username, password, real_name, role) VALUES
('ga_admin', 'admin123', '總務部管理員', 'GA'),
('hr_admin', 'admin123', '人事部管理員', 'HR'),
('employee1', 'user123', '張小明', 'EMPLOYEE');

-- 插入測試公告
INSERT INTO BSNotices (main_title, sub_title, content, category, file_path, file_name, publisher_id, publish_date) VALUES
('2026年 系統維護通知', '伺服器例行檢查與網路維護作業', '總務部將於本週六凌晨進行伺服器升級，作業期間相關系統將暫停服務。詳情請參閱附件說明。', 'GA', '/uploads/notices/maintenance_notice.pdf', '維護作業細則.pdf', 1, '2026-09-01'),
('人事評核制度更新', '2026下半年績效考核發布', '請全體員工於本月末前至系統完成自我評核作業，詳細評核標準請點擊下載附件檔。', 'HR', '/uploads/notices/performance_eval.docx', '2026績效評核表.docx', 2, '2026-09-01');