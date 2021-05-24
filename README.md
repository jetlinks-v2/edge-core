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
