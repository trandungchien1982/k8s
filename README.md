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

- Ta sẽ deploy 1 service chính `main-service` để giao tiếp với OuterNetwork. 
Bản thân `main-service` sẽ giao tiếp RESTful APIs nội bộ với các service khác trong cluster k8s để lấy thông tin

- Bản thân k3d đã tạo 1 Proxy Server để mapping port `80` của Cluster ra port `7100` của laptop/desktop

- Truy cập vào `main-service` từ laptop/desktop như sau:<br/>
    http://localhost:7100/main-info<br/>
	
- Sơ đồ truy cập request như sau : 

```shell script
                FORWARD                   Ingress          Service(:9237)+LB
localhost:7100 ---------> ProxyServer:80 --------> Cluster ------------------> Pods(main-service:9237)


[Bên trong Cluster K8S, Private Call RESTful APIs]
              (http://s2s-user-service:9238)          Service(:9238)+LB
main-service -------------------------------> Cluster ------------------> Pods(user-service:9238)

              (http://s2s-book-service:9239)          Service(:9239)+LB
main-service -------------------------------> Cluster ------------------> Pods(book-service:9239)


              http://s2s-book-sub-service-1:9241)          Service(:9241)+LB
book-service ------------------------------------> Cluster ------------------> Pods(book-sub1-service:9241)

              http://s2s-book-sub-service-2:9242)          Service(:9242)+LB
book-service ------------------------------------> Cluster ------------------> Pods(book-sub2-service:9242)

              http://s2s-book-sub-service-3:9243)          Service(:9243)+LB
book-service ------------------------------------> Cluster ------------------> Pods(book-sub3-service:9243)

```

- Apply manifiest vào K8S
```shell script
./02.deploy-services-k8s.sh
```

- Xóa resource
```shell script
./03.delete-services-k8s.sh
```