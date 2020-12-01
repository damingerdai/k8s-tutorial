# 使用k8s运行一个单实例有状态应用

## 部署

### 部署pv和pvc

```bash
kubectl apply -f mysql-pv.yaml
```

### 部署deployment

```bash
kubectl apply -f mysql-deployment.yaml
```

### 展示Deployment相关信息

```bash
kubectl describe deployment mysql
```

### 列举Deployment创建的pods

```bash
kubectl get pods -l app=mysql
```

### 查看PersistentVolumeClaim

```bash
kubectl describe pvc mysql-pv-claim
```

### 运行 MySQL 客户端以连接到服务器

```bash
kubectl run -it --rm --image=mysql:5.6 --restart=Never mysql-client -- mysql -h mysql -ppassword
```

## 删除

```bash
kubectl delete deployment,svc mysql
kubectl delete pvc mysql-pv-claim
kubectl delete pv mysql-pv-volume
```

## 参考
[运行一个单实例有状态应用](https://kubernetes.io/zh/docs/tasks/run-application/run-single-instance-stateful-application/)
