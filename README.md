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
    - Cluster sử dụng Traefik LoadBalancer
    - Mapping cổng 80 của Cluster ra máy laptop/desktop ở cổng 7100<br/>
        (Nghĩa là http://localhost:7100 <=> http://{k3s-dev-cluster}:80)
    - Cổng giao tiếp 443 (SSL) của Load Balancer sẽ map qua cổng 7143 của laptop/desktop
    - Master Node sẽ mở cổng 6443 để expose các k8s APIs
    - Cluster bao gồm 1 Master Node + 3 Worker Nodes
    - Sử dụng chung 1 network `common-network` trong Docker để có thể contact với các Docker service khác (nếu muốn)
    - Token dùng để join Worker Node mới là `K3D_K3S_CHIEN_TOKEN`
```
    k3d cluster create k3s-dev --port 7100:80@loadbalancer --port 7143:443@loadbalancer --api-port 6443 --servers 1 --agents 3 --network common-network --token K3D_K3S_CHIEN_TOKEN
```
```shell script
INFO[0000] portmapping '7100:80' targets the loadbalancer: defaulting to [servers:*:proxy agents:*:proxy]
INFO[0000] portmapping '7143:443' targets the loadbalancer: defaulting to [servers:*:proxy agents:*:proxy]
INFO[0000] Prep: Network
INFO[0000] Re-using existing network 'common-network' (20ed1611a6f99ca8460eb8972eeebf98f7b1cac5de77daede683f5cffe548915)
INFO[0000] Created volume 'k3d-k3s-dev-images'
INFO[0000] Starting new tools node...
INFO[0000] Starting Node 'k3d-k3s-dev-tools'
INFO[0001] Creating node 'k3d-k3s-dev-server-0'
INFO[0001] Creating node 'k3d-k3s-dev-agent-0'
INFO[0001] Creating node 'k3d-k3s-dev-agent-1'
INFO[0001] Creating node 'k3d-k3s-dev-agent-2'
INFO[0002] Creating LoadBalancer 'k3d-k3s-dev-serverlb'
INFO[0002] Using the k3d-tools node to gather environment information
INFO[0004] Starting cluster 'k3s-dev'
INFO[0004] Starting servers...
INFO[0004] Starting Node 'k3d-k3s-dev-server-0'
INFO[0010] Starting agents...
INFO[0010] Starting Node 'k3d-k3s-dev-agent-2'
INFO[0010] Starting Node 'k3d-k3s-dev-agent-1'
INFO[0010] Starting Node 'k3d-k3s-dev-agent-0'
INFO[0023] Starting helpers...
INFO[0023] Starting Node 'k3d-k3s-dev-serverlb'
INFO[0030] Injecting '192.168.65.2 host.k3d.internal' into /etc/hosts of all nodes...
INFO[0030] Injecting records for host.k3d.internal and for 9 network members into CoreDNS configmap...                                                                                                    INFO[0032] Cluster 'k3s-dev' created successfully!
INFO[0032] You can now use it like this: 
        kubectl cluster-info
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

# Ví dụ [01.HelloWorld]
==============================================================

Ta sẽ deploy `nginx` example như sau:
- App `nginx` xuất ra port 80, map ra port của pod là 80
- Các pods được gắn label là `app: main-service-hello-world`
- Tạo Service để móc nối request tới Pods dựa theo label `app: main-service-hello-world`, service mở cổng `5100` và mapping vào cổng `80` của pods đã chọn
- Tạo Ingress để mapping cổng `80` của Cluster với cổng `5100` của Service
- Bản thân k3d đã tạo 1 Proxy Server để mapping port `80` của Cluster ra port `7100` của laptop/desktop

https://doc.traefik.io/traefik/providers/kubernetes-ingress/<br/>
(Sử dụng Ingress Traefik Controller)

Ingress dùng để tạo quy tắc traffic HTTP/HTTPS request từ public network vào trong Cluster
Nó đóng vai trò như một Proxy Server (API Gateway cho Cluster)

Thông thường Ingress sẽ điều hướng request từ bên ngoài vào trong service chính của Cluster mà thôi.
Các services khác bên trong Cluster sẽ nói chuyện với nhau thông qua kind:Service,
	thường dùng ClusterIP để giao tiếp với nhau.
	
Truy cập vào service chính của Cluster:
	http://localhost:7100
  
```shell script
                FORWARD                   Ingress           Service(:5100)+LB
localhost:7100 ---------> ProxyServer:80 --------> Cluster ------------------> Pods(nginx:80)
```

- Phần Ingress tạo bằng YAML
```shell script
kubectl apply -f deploy-nginx-hello-world.yaml
```

- Xóa resource
```shell script
kubectl delete -f deploy-nginx-hello-world.yaml
```