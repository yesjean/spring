# 데이터 베이스 생성

### 생성
```
CREATE DATABASE my_database;
```

### 선택
```
USE my_database;
```

#

# 테이블 생성


### users 테이블 생성
```
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    email_id VARCHAR(255),
    last_name VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE, -- 기본값을 TRUE로 설정
    role VARCHAR(255) NOT NULL
);
```
### user_roles 테이블 생성
```
CREATE TABLE user_roles (
    username VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (username, role),
    FOREIGN KEY (username) REFERENCES users(username)
);
```
### posts 테이블 생성
```
CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    image_path VARCHAR(255)
);
```

### 외래 키 제약 조건 설정 변경

```
ALTER TABLE user_roles 
DROP FOREIGN KEY user_roles_ibfk_1;

ALTER TABLE user_roles 
ADD CONSTRAINT user_roles_ibfk_1 FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE;
```

### 관리자 계정 만들기

```
INSERT INTO users (username, password, name, email, role, enabled) 
VALUES ('admin', '1234', 'Administrator', 'admin@example.com', 'ROLE_ADMIN', TRUE);
```

# mssql

### MySQL에 로그인

```
mysql -u root -p
```

### 비밀번호 변경방법

```
ALTER USER 'root'@'localhost' IDENTIFIED BY '새로운비밀번호';
```

### 변경 사항 적용
변경 사항을 적용하기 위해 다음 명령어를 실행합니다.
```
FLUSH PRIVILEGES;
```

### MySQL 종료
MySQL을 종료합니다.

```
EXIT;
```

# 기능

### 로그인 로그아웃

`http://localhost:8080/login`

### 게시물 crud

`http://localhost:8080/posts`

### 관리자 페이지