# 自定义表单系统

这是一个功能完整的自定义表单系统，基于Vue3 + Ant Design Vue 4和Spring Boot + MyBatis Plus构建，支持拖拽式表单设计、表单版本管理、数据可视化和Excel导出等功能。

## 系统功能

1. 拖拽式表单设计器
2. 表单配置管理和版本控制
3. 表单数据录入和管理
4. 数据可视化展示（ECharts图表）
5. Excel数据导出功能
6. 多条件数据搜索和分页
7. 字段格式化器系统

## 技术栈

### 后端
- Spring Boot 2.7.0
- MyBatis Plus 3.5.2
- MySQL 8.0
- Maven 3.8+
- Apache POI (Excel处理)

### 前端
- Vue 3.4
- Ant Design Vue 4.x
- Vite 5.0
- ECharts 5.x
- Axios
- Vue Router 4

## 环统环境

### 后端环境
1. 安装JDK 8或以上版本
2. 安装MySQL 8.0
3. 安装Maven 3.8+

### 前端环境
1. 安装Node.js 16或以上版本
2. 安装npm或yarn

## 快速开始

### 1. 数据库配置
```sql
-- 创建数据库
CREATE DATABASE form_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行数据库脚本
source database/form_system.sql
```

### 2. 后端启动
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将运行在 `http://localhost:8080`

### 3. 前端启动
```bash
cd form-system
npm install
npm run dev
```

前端服务将运行在 `http://localhost:5173`

## 系统使用说明

### 1. 表单设计
- 访问系统首页
- 点击"新建表单"进入表单设计器
- 从左侧组件库拖拽组件到设计区域
- 配置表单和字段属性
- 保存表单

### 2. 表单发布
- 在表单列表中找到需要发布的表单
- 点击"发布"按钮
- 发布后的表单可以用于数据录入

### 3. 数据录入
- 在表单列表中点击"数据管理"
- 点击"新增数据"录入表单数据
- 可以对已有数据进行编辑和删除
- 支持单条件和多条件搜索
- 支持分页浏览数据

### 4. 数据统计
- 在表单列表中点击"数据统计"
- 选择图表类型和字段
- 系统将生成相应的数据可视化图表

### 5. Excel导出
- 在数据管理页面点击"导出Excel"按钮
- 系统将根据当前搜索条件导出数据
- 导出的文件包含格式化后的字段值

## API接口文档

### 表单配置相关接口
- `GET /api/form/config/list` - 获取所有表单配置
- `GET /api/form/config/{id}` - 根据ID获取表单配置
- `GET /api/form/config/{formKey}` - 根据formKey获取表单配置
- `GET /api/form/config/versions/{formKey}` - 根据formKey获取所有版本的表单配置
- `POST /api/form/config/create` - 创建表单配置
- `POST /api/form/config/update/{formKey}` - 更新表单配置（创建新版本）
- `DELETE /api/form/config/delete/{id}` - 删除表单配置
- `PUT /api/form/config/publish/{id}` - 发布表单
- `PUT /api/form/config/disable/{id}` - 停用表单
- `PUT /api/form/config/enable/{id}` - 启用表单
- `PUT /api/form/config/rollback/{formKey}/{version}` - 回滚到指定版本

### 表单字段相关接口
- `GET /api/form/field/list/{formId}` - 根据表单ID获取所有字段配置
- `GET /api/form/field/list/key/{formKey}` - 根据表单Key获取所有字段配置
- `GET /api/form/field/list/key/{formKey}/version/{version}` - 根据表单Key和版本号获取字段配置
- `POST /api/form/field/create` - 创建字段配置
- `POST /api/form/field/batch-create` - 批量创建字段配置
- `PUT /api/form/field/update` - 更新字段配置
- `DELETE /api/form/field/delete/{id}` - 删除字段配置

### 表单数据相关接口
- `POST /api/form/data/save/{formKey}` - 保存表单数据
- `PUT /api/form/data/update/{formKey}/{dataId}` - 更新表单数据
- `DELETE /api/form/data/delete/{formKey}/{dataId}` - 删除表单数据
- `GET /api/form/data/list/{formKey}` - 查询表单数据列表（支持搜索和分页）
- `GET /api/form/data/{formKey}/all` - 查询所有版本的表单数据列表
- `GET /api/form/data/list/{formKey}/version/{version}` - 查询指定版本的表单数据列表
- `GET /api/form/data/{formKey}/{dataId}` - 根据ID查询表单数据
- `GET /api/form/data/{formKey}/{dataId}/version/{version}` - 根据ID和版本查询表单数据
- `POST /api/form/data/search/{formKey}` - 多条件查询表单数据
- `GET /api/form/data/export/{formKey}` - 导出表单数据为Excel

### 数据统计相关接口
- `GET /api/form/statistics/overview/{formKey}` - 获取表单数据统计信息（最新版本）
- `GET /api/form/statistics/overview/{formKey}/version/{version}` - 获取表单数据统计信息（指定版本）
- `GET /api/form/statistics/overview-all/{formKey}` - 获取表单数据统计信息（所有版本）
- `GET /api/form/statistics/chart/{formKey}` - 获取图表数据（最新版本）
- `GET /api/form/statistics/chart/{formKey}/version/{version}` - 获取图表数据（指定版本）
- `GET /api/form/statistics/chart-all/{formKey}` - 获取图表数据（所有版本）

## 项目结构

```
form/
├── backend/              # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/form/system/
│   │   │   │       ├── controller/    # 控制器
│   │   │   │       ├── entity/        # 实体类
│   │   │   │       ├── mapper/        # Mapper接口
│   │   │   │       ├── service/       # 服务接口
│   │   │   │       ├── service/impl/  # 服务实现
│   │   │   │       ├── formatter/     # 字段格式化器
│   │   │   │       └── utils/         # 工具类
│   │   │   └── resources/             # 配置文件
│   └── pom.xml                        # Maven配置
├── form-system/         # 前端项目
│   ├── src/
│   │   ├── api/         # API接口
│   │   ├── components/  # 组件
│   │   ├── router/      # 路由配置
│   │   ├── views/       # 页面视图
│   │   ├── utils/       # 工具类
│   │   ├── App.vue      # 根组件
│   │   └── main.js      # 入口文件
│   └── package.json     # 依赖配置
└── database/            # 数据库脚本
    └── form_system.sql
```

## 开发指南

### 后端开发
1. 控制器层：处理HTTP请求，调用服务层
2. 服务层：实现业务逻辑
3. Mapper层：数据库操作
4. 实体类：数据模型
5. 字段格式化器：处理字段值的格式化和解析

### 前端开发
1. Views：页面组件
2. Components：可复用组件
3. API：后端接口调用
4. Router：路由配置
5. Utils：工具类（包括字段格式化器）

## 部署说明

### 后端部署
```bash
cd backend
mvn clean package
java -jar target/form-system-0.0.1-SNAPSHOT.jar
```

### 前端部署
```bash
cd form-system
npm run build
# 将dist目录部署到Web服务器
```

## 常见问题

1. **数据库连接失败**
   - 检查application.yml中的数据库配置
   - 确认MySQL服务是否启动
   - 检查数据库用户权限

2. **前端页面空白**
   - 检查后端服务是否正常运行
   - 检查浏览器控制台错误信息
   - 确认API接口地址配置正确

3. **图表不显示**
   - 检查ECharts是否正确引入
   - 确认数据格式是否正确

4. **Excel导出失败**
   - 检查后端Apache POI依赖是否正确配置
   - 确认字段格式化器是否正常工作

## 许可证

MIT License