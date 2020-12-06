# Kubernetes 对象管理

kubectl 命令行工具支持多种不同的方式来创建和管理 Kubernetes 对象

> :warning: 应该只使用一种技术来管理 Kubernetes 对象。混合和匹配技术作用在同一对象上将导致未定义行为。

| Management technique             | Operates on          | Recommended environment | Supported writers | Learning curve |
| ---                              | ---                  | --                      | --                | --             |
| Imperative commands              | Live objects         | Development projects    | 1+                | Lowest         |
| Imperative object configuration  | Individual files     | Production projects     | 1                 | Moderate       |
| Declarative object configuration | Directories of files | Production projects     | 1+                | Highest        |

## 命令式命令
使用命令式命令时，用户可以在集群中的活动对象上进行操作。用户将操作传给 kubectl 命令作为参数或标志。

这是开始或者在集群中运行一次性任务的最简单方法。因为这个技术直接在活动对象上操作，所以它不提供以前配置的历史记录。

### 例子

通过创建 Deployment 对象来运行 nginx 容器的实例：

```bash
kubectl run nginx --image nginx
```

使用不同的语法来达到同样的上面的效果：

```
kubectl create deployment nginx --image nginx
```

### 权衡利弊

与对象配置相比的优点：

1. 命令简单，易学且易于记忆。
2. 命令仅需一步即可对集群进行更改。

与对象配置相比的缺点：

1. 命令不与变更审查流程集成。
2. 命令不提供与更改关联的审核跟踪。
3. 除了实时内容外，命令不提供记录源。
4. 命令不提供用于创建新对象的模板。

## 命令式对象配置

在命令式对象配置中，kubectl 命令指定操作（创建，替换等），可选标志和至少一个文件名。指定的文件必须包含 YAML 或 JSON 格式的对象的完整定义。

> :warning: replace 命令式命令将现有规范替换为新提供的规范，并删除对配置文件中缺少的对象的所有更改。此方法不应与规范独立于配置文件进行更新的资源类型一起使用。

### 例子

创建配置文件中定义的对象：

```bash
kubectl create -f nginx.yaml
```

删除两个配置文件中定义的对象：

```bash
kubectl delete -f nginx.yaml -f redis.yaml
```

通过覆盖活动配置来更新配置文件中定义的对象：

```bash
kubectl replace -f nginx.yaml
```

### 权衡利弊

与命令式命令相比的优点：

1. 对象配置可以存储在源控制系统中，比如 Git。
2. 对象配置可以与流程集成，例如在推送和审计之前检查更新。
3. 对象配置提供了用于创建新对象的模板。

与命令式命令相比的缺点：

1. 对象配置需要对对象架构有基本的了解。
2. 对象配置需要额外的步骤来编写 YAML 文件。

与声明式对象配置相比的优点：

1. 命令式对象配置行为更加简单易懂
2. 从 Kubernetes 1.5 版本开始，命令式对象配置更加成熟。

与声明式对象配置相比的缺点：

1. 命令式对象配置更适合文件，而非目录。
2. 对活动对象的更新必须反映在配置文件中，否则会在下一次替换时丢失。

## 声明式对象配置

使用声明式对象配置时，用户对本地存储的对象配置文件进行操作，但是用户未定义要对该文件执行的操作。kubectl 会自动检测每个文件的创建、更新和删除操作。这使得配置可以在目录上工作，根据目录中配置文件对不同的对象执行不同的操作。

> 声明式对象配置保留其他编写者所做的修改，即使这些更改并未合并到对象配置文件中。可以通过使用 patch API 操作仅写入观察到的差异，而不是使用 replace API 操作来替换整个对象配置来实现。

### 例子

处理 configs 目录中的所有对象配置文件，创建并更新活动对象。可以首先使用 diff 子命令查看将要进行的更改，然后在进行应用：

```bash
kubectl diff -f configs/
kubectl apply -f configs/
```

递归处理目录：

```
kubectl diff -R -f configs/
kubectl apply -R -f configs/
```

### 权衡利弊

与命令式对象配置相比的优点：

1. 对活动对象所做的更改即使未合并到配置文件中，也会被保留下来。
2. 声明性对象配置更好地支持对目录进行操作并自动检测每个文件的操作类型（创建，修补，删除）。

与命令式对象配置相比的缺点：

1. 声明式对象配置难于调试并且出现异常时结果难以理解。
2. 使用 diff 产生的部分更新会创建复杂的合并和补丁操作。
