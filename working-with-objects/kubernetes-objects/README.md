# 理解 Kubernetes 对象

在 Kubernetes 系统中，Kubernetes 对象 是持久化的实体。 Kubernetes 使用这些实体去表示整个集群的状态。特别地，它们描述了如下信息：

* 哪些容器化应用在运行（以及在哪些节点上）
* 可以被应用使用的资源
* 关于应用运行时表现的策略，比如重启策略、升级策略，以及容错策略

## 对象规约（Spec）与状态（Status）

几乎每个 Kubernetes 对象包含两个嵌套的对象字段，它们负责管理对象的配置： 对象 spec（规约） 和 对象 status（状态） 。 对于具有 spec 的对象，你必须在创建对象时设置其内容，描述你希望对象所具有的特征： 期望状态（Desired State)。status 描述了对象的 当前状态（Current State），它是由 Kubernetes 系统和组件 设置并更新的。

### 示例

这里有一个 .yaml 示例文件，展示了 Kubernetes Deployment 的必需字段和对象规约：


```
apiVersion: apps/v1 # for versions before 1.9.0 use apps/v1beta2
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 2 # tells deployment to run 2 pods matching the template
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.14.2
        ports:
        - containerPort: 80

```

#### 运行

```bash
kubectl apply -f deployment.yaml --record
```

输出：

```bash
deployment.apps/nginx-deployment created
```

#### 查看

```bash
kubectl describe deployment nginx-deployment
kubectl get pods -l app=nginx
```

#### 删除

```bash
 kubectl delete deployment --all
 ```
