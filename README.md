# 无人机商业飞行审批服务

基于 Spring Boot 和 PostgreSQL 的无人机商业飞行审批管理系统。

## 原始需求

> 请开发无人机商业飞行审批服务，使用 Spring Boot 和 PostgreSQL 管理飞手、无人机、任务、航线、空域、保险、禁飞区、审批意见、起飞报备和飞行日志。企业提交航拍、巡检、测绘或应急勘查任务，飞手上传资质、机型、电池、保险和计划航线，审批人员检查空域、时间、高度、半径、天气和临时管制，飞行后回传轨迹与异常说明。服务要处理航线跨区、禁飞区更新、天气取消、重复报备、多人共用飞机、日志缺失和高度超限，让审批拒绝时能指出具体冲突段。

## 项目简介

本系统提供完整的无人机商业飞行审批全流程管理，包括：

- **飞手管理**：飞手资质、等级、执照有效期管理
- **无人机管理**：机型、电池、序列号、状态管理
- **保险管理**：无人机保单管理和有效期校验
- **空域管理**：管控空域、受限空域查询
- **禁飞区管理**：永久/临时禁飞区、机场/军事/政府禁飞区
- **任务管理**：航拍、巡检、测绘、应急勘查任务的创建、提交、取消
- **航线管理**：计划航线、航点、跨区检测
- **审批管理**：审批通过/拒绝/退回，冲突段标注
- **起飞报备**：起飞前报备，重复报备检测
- **飞行日志**：轨迹回传、异常说明、高度超限检测、日志缺失记录

## 技术栈

- Java 17
- Spring Boot 3.4.3
- Spring Data JPA
- PostgreSQL 16
- Flyway（数据库迁移）
- H2（内存数据库，用于测试）
- Maven

> **注意**：由于 JDK 25 与 Lombok 存在兼容性问题，项目已移除 Lombok 依赖，所有实体类和 DTO 类的 getter/setter 方法均已手动实现。

## 启动方式

### 前置要求

- JDK 17+
- Maven 3.9+
- PostgreSQL 16+
- Docker & Docker Compose（可选，用于一键启动）

### Docker 一键启动（推荐）

#### 1. 构建并启动服务

```bash
docker compose up --build
```

后台运行：

```bash
docker compose up --build -d
```

#### 2. 查看服务日志

```bash
docker compose logs -f app
```

#### 3. 停止并清理服务

```bash
docker compose down
```

如需同时清理数据卷：

```bash
docker compose down -v
```

**访问地址**：http://localhost:8080/api

### 本地手动启动

#### 1. 准备 PostgreSQL 数据库

确保 PostgreSQL 服务已启动，创建数据库：

```sql
CREATE DATABASE drone_approval;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE drone_approval TO postgres;
```

#### 2. 配置环境变量（可选）

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=drone_approval
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

#### 3. 安装依赖并构建

```bash
mvn clean package -DskipTests
```

#### 4. 启动服务

```bash
java -jar target/drone-flight-approval-1.0.0.jar
```

**访问地址**：http://localhost:8080/api

## API 概览

所有接口前缀：`/api`

### 飞手管理 `/pilots`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /pilots | 获取所有飞手 |
| GET | /pilots/{id} | 根据ID获取飞手 |
| GET | /pilots/license/{licenseNumber} | 根据执照号获取飞手 |
| POST | /pilots | 创建飞手 |
| PUT | /pilots/{id} | 更新飞手 |
| DELETE | /pilots/{id} | 停用飞手 |

### 无人机管理 `/drones`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /drones | 获取所有无人机 |
| GET | /drones/{id} | 根据ID获取无人机 |
| GET | /drones/serial/{serialNumber} | 根据序列号获取无人机 |
| POST | /drones | 创建无人机 |
| PUT | /drones/{id} | 更新无人机 |
| PUT | /drones/{id}/status | 更新无人机状态 |
| DELETE | /drones/{id} | 退役无人机 |

### 保险管理 `/insurance`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /insurance | 获取所有保险 |
| GET | /insurance/{id} | 根据ID获取保险 |
| GET | /insurance/drone/{droneId} | 获取无人机的所有保险 |
| GET | /insurance/drone/{droneId}/valid | 获取无人机有效保险 |
| POST | /insurance | 创建保险 |
| PUT | /insurance/{id} | 更新保险 |
| PUT | /insurance/{id}/cancel | 取消保险 |

### 空域管理 `/airspaces`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /airspaces | 获取所有空域 |
| GET | /airspaces/{id} | 根据ID获取空域 |
| GET | /airspaces/code/{code} | 根据编码获取空域 |
| GET | /airspaces/nearby | 查询附近空域 |
| GET | /airspaces/restricted | 获取受限空域 |

### 禁飞区管理 `/no-fly-zones`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /no-fly-zones | 获取所有禁飞区 |
| GET | /no-fly-zones/{id} | 根据ID获取禁飞区 |
| GET | /no-fly-zones/active | 获取当前有效禁飞区 |
| POST | /no-fly-zones | 创建禁飞区 |
| PUT | /no-fly-zones/{id} | 更新禁飞区 |
| PUT | /no-fly-zones/{id}/cancel | 取消禁飞区 |

### 任务管理 `/missions`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /missions | 获取所有任务 |
| GET | /missions/{id} | 根据ID获取任务 |
| GET | /missions/number/{missionNumber} | 根据任务编号获取 |
| GET | /missions/pilot/{pilotId} | 获取飞手的任务 |
| GET | /missions/drone/{droneId} | 获取无人机的任务 |
| GET | /missions/status/{status} | 按状态获取任务 |
| GET | /missions/{id}/conflicts | 检测任务冲突 |
| POST | /missions | 创建任务（含航线） |
| PUT | /missions/{id} | 更新任务 |
| PUT | /missions/{id}/submit | 提交审批 |
| PUT | /missions/{id}/cancel | 取消任务 |
| DELETE | /missions/{id} | 删除草稿任务 |

### 审批管理 `/approvals`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /approvals/mission/{missionId} | 获取任务的所有审批 |
| GET | /approvals/mission/{missionId}/latest | 获取最新审批 |
| GET | /approvals/{id} | 根据ID获取审批 |
| GET | /approvals/{id}/conflicts | 获取审批的冲突段 |
| POST | /approvals | 创建审批（可标注冲突段） |

### 起飞报备 `/takeoff-reports`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /takeoff-reports/mission/{missionId} | 获取任务的报备记录 |
| GET | /takeoff-reports/{id} | 根据ID获取报备 |
| POST | /takeoff-reports | 提交起飞报备（检测重复） |

### 飞行日志 `/flight-logs`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /flight-logs/mission/{missionId} | 获取任务的飞行日志 |
| GET | /flight-logs/{id} | 根据ID获取日志 |
| GET | /flight-logs/mission/{missionId}/latest | 获取最新日志 |
| GET | /flight-logs/{id}/conflicts | 检测日志冲突 |
| POST | /flight-logs | 提交飞行日志 |
| PUT | /flight-logs/{id} | 更新飞行日志 |

## 核心业务逻辑

### 冲突检测

系统支持以下冲突类型检测：

- **NO_FLY_ZONE**：航线或轨迹进入禁飞区
- **AIRSPACE_RESTRICTION**：进入受限或禁止空域
- **HEIGHT_EXCEEDED**：高度超限
- **TIME_CONFLICT**：时间冲突（结束早于开始、早于当前时间）
- **TEMPORARY_RESTRICTION**：临时管制（如无有效保险）
- **WEATHER_UNSUITABLE**：天气不适合飞行
- **CROSS_REGION**：航线跨区域（相邻航点距离 > 50公里）
- **DRONE_CONFLICT**：同一无人机在相同时间段被多个任务占用

### 审批拒绝冲突段标注

审批拒绝时，可通过 `conflictSegments` 字段标注具体冲突段，包含：
- 冲突类型
- 起止航点序号
- 起止经纬度
- 冲突高度
- 冲突区域名称和编码
- 详细描述

### 重复报备检测

同一任务在5分钟内的起飞报备会被标记为重复报备，并抛出异常提示。

### 多人共用飞机检测

提交任务时自动检测无人机时间占用冲突，若同一时间段无人机已被其他已审批/进行中任务占用则报告冲突。

### 日志缺失处理

飞行日志支持 `LOG_MISSING` 状态，必须提供缺失原因。

### 高度超限检测

- 任务审批时：检测航线高度是否超过任务计划最大高度
- 日志提交后：检测实际轨迹高度是否超过审批最大高度

## 目录结构

```
src/main/java/com/drone/approval/
├── DroneFlightApprovalApplication.java
├── common/
│   └── ApiResponse.java
├── controller/
│   ├── PilotController.java
│   ├── DroneController.java
│   ├── InsuranceController.java
│   ├── AirspaceController.java
│   ├── NoFlyZoneController.java
│   ├── MissionController.java
│   ├── ApprovalController.java
│   ├── TakeoffReportController.java
│   └── FlightLogController.java
├── dto/
│   ├── PilotRequest.java
│   ├── DroneRequest.java
│   ├── InsuranceRequest.java
│   ├── NoFlyZoneRequest.java
│   ├── MissionRequest.java
│   ├── ApprovalRequest.java
│   ├── TakeoffReportRequest.java
│   ├── FlightLogRequest.java
│   └── ConflictCheckResult.java
├── entity/
│   ├── Pilot.java
│   ├── Drone.java
│   ├── Insurance.java
│   ├── Airspace.java
│   ├── NoFlyZone.java
│   ├── Mission.java
│   ├── FlightRoute.java
│   ├── RoutePoint.java
│   ├── Approval.java
│   ├── ConflictSegment.java
│   ├── TakeoffReport.java
│   ├── FlightLog.java
│   └── TrackPoint.java
├── exception/
│   ├── BusinessException.java
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── PilotRepository.java
│   ├── DroneRepository.java
│   ├── InsuranceRepository.java
│   ├── AirspaceRepository.java
│   ├── NoFlyZoneRepository.java
│   ├── MissionRepository.java
│   ├── FlightRouteRepository.java
│   ├── ApprovalRepository.java
│   ├── ConflictSegmentRepository.java
│   ├── TakeoffReportRepository.java
│   └── FlightLogRepository.java
├── service/
│   ├── PilotService.java
│   ├── DroneService.java
│   ├── InsuranceService.java
│   ├── AirspaceService.java
│   ├── NoFlyZoneService.java
│   ├── MissionService.java
│   ├── ApprovalService.java
│   ├── TakeoffReportService.java
│   ├── FlightLogService.java
│   └── ConflictDetectionService.java
└── util/
    └── GeoUtils.java
```
