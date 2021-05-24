# 边缘网关核心模块


## 使用

1. 关联模块到拓展模块中

```bash

$  git submodule add --force git@github.com:jetlinks/edge-core.git expands-components/jetlinks-edge/edge-core

```

2. 在项目根目录下的`pom.xml`中的`modules`节点中添加模块

```xml
<modules>
    <module>expands-components/jetlinks-edge/edge-core</module>
</modules>
```

3. 在启动模块中引入依赖

```xml
<dependency>
    <groupId>org.jetlinks.pro</groupId>
    <artifactId>edge-master</artifactId>
    <version>${project.version}</version>
</dependency>
```

4. 重新编译启动即可
