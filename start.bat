@echo off

REM 确保有Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境。请确保已安装JDK 8或更高版本并配置了环境变量。
    pause
    exit /b 1
)

REM 确保有Maven环境
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven环境。请确保已安装Maven并配置了环境变量。
    pause
    exit /b 1
)

REM 清理并构建项目
echo 正在构建项目...
mvn clean install
if %errorlevel% neq 0 (
    echo 错误: 项目构建失败。
    pause
    exit /b 1
)

REM 运行Spring Boot应用
echo 正在启动应用...
mvn spring-boot:run

pause