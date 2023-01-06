# ChinaRegionJava
[国家统计局] 最全最新中国省，市，地区数据库，Java Spring Boot RESTful 服务！


## 数据来源
[国家统计局]全国统计用区划代码和城乡划分代码
http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/index.html

## 使用方法

#### 1，获取省份列表
请求
```
GET /api/v1/chinaRegion/province/list
```
响应
```json
{
  "status": 200,
  "service": "",
  "version": "1.0",
  "error": "",
  "message": "",
  "traceId": "",
  "timestamp": 1672928929975,
  "body": 
  {
    "provinceList": 
    [
      {
        "province": "北京市",
        "city": "",
        "name": "北京市",
        "id": "110000000000"
      },
      {
        "province": "天津市",
        "city": "",
        "name": "天津市",
        "id": "120000000000"
      }
    ]
  }
}
```

#### 2，获取城市列表
请求
```
GET /api/v1/chinaRegion/city/list/{provinceId}
```
响应
```json
{
  "status":200,
  "service":"",
  "version":"1.0",
  "error":"",
  "message":"",
  "traceId":"",
  "timestamp":1672929254143,
  "body":
  {
    "cityList":
    [
      {
        "province": "山东省",
        "city":"",
        "name": "济南市",
        "id": "370100000000"
      },
      {
        "province": "山东省",
        "city":"",
        "name": "青岛市",
        "id": "370200000000"
      }
    ]
  }
}
```
#### 3，获取区县列表
请求
```
GET /api/v1/chinaRegion/district/list/{cityId}
```
响应
```json
{
  "status": 200,
  "service": "",
  "version": "1.0",
  "error": "",
  "message": "",
  "traceId": "",
  "timestamp": 1672929629562,
  "body": 
  {
    "districtList": 
    [
      {
        "province": "",
        "city": "辽阳市",
        "name": "市辖区",
        "id": "211001000000"
      },
      {
        "province": "",
        "city": "辽阳市",
        "name": "白塔区",
        "id": "211002000000"
      }
    ]
  }
}
```

## JSON数据
```
src/main/resources/chinaRegions/

├── province.json   # 省份
├── city.json       # 城市
├── district.json   # 区域/县

```


## 更新记录

### #2023.01.05
```
1，整理编写代码
2，整理编写单元测试
```
