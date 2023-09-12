# k8s
Các ví dụ liên quan đến việc k8s từ cơ bản đến nâng cao

Mỗi nhánh trong Repo sẽ là 1 ví dụ/ giải pháp/ project mẫu trong Kubernetes

# Môi trường phát triển
- K3D + K3S dùng tạo cluster k8s để thực hành
```
    docker version
------------------------------------------------------------
    Version:           20.10.11
    Server: Docker Engine - Community
```

- K3D + K3S version
```
    k3d version
------------------------------------------------------------
    k3d version v5.2.2
    k3s version v1.21.7-k3s1 (default)
```

- Tạo k8s cluster `k3s-dev` như sau:
```
    k3d cluster create k3s-dev --port 7100:80@loadbalancer --port 7143:443@loadbalancer --api-port 6443 --servers 1 --agents 3 --network common-network --token K3D_K3S_CHIEN_TOKEN
```

- Kiểm tra kết quả
```shell script
    kubectl get nodes
-----------------------------------------------------------
NAME                   STATUS   ROLES                  AGE     VERSION
k3d-k3s-dev-server-0   Ready    control-plane,master   7m35s   v1.21.7+k3s1
k3d-k3s-dev-agent-0    Ready    <none>                 7m32s   v1.21.7+k3s1
k3d-k3s-dev-agent-1    Ready    <none>                 7m32s   v1.21.7+k3s1
k3d-k3s-dev-agent-2    Ready    <none>                 7m29s   v1.21.7+k3s1

```

# Folder liên quan trên Windows
```
D:\Projects\k8s
```
==============================================================

# Ví dụ [03.ServiceCallService]
==============================================================

- Ta sẽ deploy 1 service chính `main-service` để giao tiếp với OuterNetwork:
(`main-service` sử dụng )
    - Request đến `http://restful-service1.com` thì sẽ forward qua `service-1` (public LB)
    - Request đến `http://restful-service2.com` thì sẽ forward qua `service-2` (public LB)
    - Ta có thể giả lập 2 tên miền trên trong file /etc/hosts (Unbuntu 20.04) hoặc C:\Windows\system32\drivers\etc\hosts
    ```shell script
        127.0.0.1 restful-service1.com
        127.0.0.1 restful-service2.com
    ```

- Bản thân k3d đã tạo 1 Proxy Server để mapping port `80` của Cluster ra port `7100` của laptop/desktop

- Truy cập vào 2 service trên từ laptop/desktop như sau:<br/>
    http://restful-service1.com:7100<br/>
    http://restful-service2.com:7100<br/>
	
- Sơ đồ truy cập request như sau : 

```shell script
                           FORWARD                   Ingress           Service(:5200)+LB
restful-service1.com:7100 ---------> ProxyServer:80 --------> Cluster ------------------> Pods(nginx:80)


                           FORWARD                   Ingress           Service(:6200)+LB
restful-service2.com:7100 ---------> ProxyServer:80 --------> Cluster ------------------> Pods(angular:80)
```

- Apply manifiest vào K8S
```shell script
kubectl apply -f deploy-2-public-services.yaml
```

- Xóa resource
```shell script
kubectl delete -f deploy-2-public-services.yaml
```