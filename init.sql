CREATE DATABASE IF NOT EXISTS task_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Change {user1} -> your user DB and password.

GRANT ALL PRIVILEGES ON task_db.* TO 'user1'@'%';
GRANT ALL PRIVILEGES ON user_db.* TO 'user1'@'%';

FLUSH PRIVILEGES;