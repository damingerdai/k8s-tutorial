# 使用Deployment运行一个无状态应用

## 部署

```bash
kubectl apply -f app.yaml
```

## 删除

删除deployment

```bash
kubectl delete deployment nginx-deployment
```

删除service
```
kubectl delete service nginx-deployment

```

#  如何从外部访问服务

## 部署

```
kubectl apply -f deployment.yaml
```

### 查看pod列表

```
kubectl get pods
```

## 将服务暴露给外部客户端的几种方式

### port-forward

通过*port-forward*转发, 这种方式在之前的文章中有提到过, 操作方便、适合调试时使用, `不适用于生产环境`.

```
kubectl port-forward --address 0.0.0.0 k8s-test-555cc695f8-2mx25 9999:8080
```

此时, 我们可以通过通过访问宿主机的*9999*端口来访问到k8s-test-555cc695f8-2mx25的*8080*端口.

```
curl http://127.0.0.1:9999/k8s-test/timestamp
```

> *port-forward*这种方式访问pod, 可以指定pod实例, 简单方便, 很适合调试之用.

### NodePort

通过*NodePort*, 此时集群中每一个节点(Node)都会监听指定端口, 我们通过任意节点的端口即可访问到指定服务. 但过多的服务会开启大量端口难以维护.

创建一个*Service*, 并指定其类型为*NodePort*.

```
kubectl apply -f service.yaml
```

查看*Service*

```
kubectl get services
```

访问*Service*

```
curl http://127.0.0.1:30080/k8s-test/timestamp
```

清除*Service*

```
kubectl delete services k8s-test-service
```

### LoadBalance

*LoadBalance(负载均衡 LB)*通常由云服务商提供. 如果环境不支持LB, 那么创建的LoadBalance将始终处于`<pending>`状态.

如果想要在本地开发环境测试LB, 我们可以选择[MetalLB](https://metallb.universe.tf/). 它是一个负载均衡实现. 安装方式此处不进行展开, 可参考官方文档.

当我们的环境支持LB时, 我们可以创建如下Service, 来暴露服务.

```
apiVersion: v1
kind: Service
metadata:
  name: k8s-test-service
spec:
  selector:
    app: k8s-test
    env: test
  ports:
    - port: 80	        # 服务端口
      targetPort: 8080	# 目标端口, 此处指的是pod的8080端口
      protocol: TCP
  type: LoadBalancer
```

### Ingress

*Ingress*公开了从群集外部到群集内*services*的HTTP和HTTPS路由. 流量路由由Ingress资源上定义的规则控制

我们使用ingress controller的nginx实现来进行测试. 首先下载部署文件

> https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/mandatory.yaml文件找不到了

### 总结

通常地, 我们会考虑*LoadBalance*和*Ingress*配合使用. 一方面是只是用LB会产生大量的花费, 另一方面大量的LB同样会提高维护成本. 而LB配合Ingress使用, 通过不同的path来区分服务能达到很棒的效果. (这里和通过Nginx来暴露多个服务的原理基本相同)


## 参考资料

1. [Kubernetes(三) 如何从外部访问服务](https://juejin.cn/post/6844903974206701575#heading-10)
