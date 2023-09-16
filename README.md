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

# Ví dụ [07.TwoPublicServicesWithDifferentNS]
==============================================================

- Ta sẽ deploy 2 services và config Ingress Treafix tùy theo domain name request đến mà sẽ forward qua service thích hợp:<br/>
  (2 services này sẽ nằm trên 2 namespace khác nhau, và chúng đều thuộc chung 1 implementation ... )
    - Request đến `http://dev.chien.com` thì sẽ forward qua service `main-service` thuộc namespace `dev-custom-ns`
    - Request đến `http://stg.chien.com` thì sẽ forward qua service `main-service` thuộc namespace `stg-custom-ns`
    - Ta có thể giả lập 2 tên miền trên trong file /etc/hosts (Unbuntu 20.04) hoặc C:\Windows\system32\drivers\etc\hosts
    ```shell script
        127.0.0.1 dev.chien.com
        127.0.0.1 stg.chien.com
    ```
  
- Ingress cần được setup để forward Traffic qua các services thuộc namespace khác nhau :
  - https://tech.aabouzaid.com/2022/08/2-ways-to-route-ingress-traffic-across-namespaces.html
  - https://stackoverflow.com/questions/59844622/ingress-configuration-for-k8s-in-different-namespaces

- View thông tin chi tiết của các namspace
  ```shell script
      kubectl get all -n dev-custom-ns
      kubectl get all -n stg-custom-ns
  ```

- Ta sử dụng Docker Image demo của nginx vì nó khá gọn nhẹ & hiển thị các thông tin request cần thiết:<br/>
  (https://hub.docker.com/r/nginxdemos/hello)

- Bản thân k3d đã tạo 1 Proxy Server để mapping port `80` của Cluster ra port `7100` của laptop/desktop

- Truy cập vào 2 service trên từ laptop/desktop như sau:<br/>
  - http://dev.chien.com:7100
  - http://stg.chien.com:7100
	
- Sơ đồ truy cập request như sau : 

```shell script
                    FORWARD                   Ingress           Service(:5270)+LB of dev-custom-ns
dev.chien.com:7100 ---------> ProxyServer:80 --------> Cluster -----------------------------------> Pods(nginx-demo:80)


                    FORWARD                   Ingress           Service(:5270)+LB of stg-custom-ns
stg.chien.com:7100 ---------> ProxyServer:80 --------> Cluster -----------------------------------> Pods(nginx-demo:80)
```

- Để điều hướng traffic vào 2 services của 2 namespace khác nhau, ta có 2 giải pháp:
  - Mỗi namespace sẽ triển khai 1 Ingress riêng với domain riêng (trong demo này)
  - Sử dụng 1 Ingress chung để điều hướng traffic qua 2 services trung gian, Service với type: ExternalService
mỗi Service trung gian này sẽ forward qua service của dev-namespacce hoặc stg-namespace

- Apply manifiest vào K8S
```shell script
  Dev Environment: 
    ./02.deploy-dev-env.sh
    ------------------------------------------------------
    kubectl apply -f deploy-dev-namespace+Ingress.yaml
    kubectl apply -f deploy-main-service-in-ns.yaml --namespace dev-custom-ns
    
  Stg Environment:
    ./03.deploy-stg-env.sh
    ------------------------------------------------------
    kubectl apply -f deploy-stg-namespace+Ingress.yaml
    kubectl apply -f deploy-main-service-in-ns.yaml --namespace stg-custom-ns
```

- Xóa resource
```shell script
    ./04.delete-all.sh
    -------------------------------------------------------
    kubectl delete all --all --namespace dev-custom-ns
    kubectl delete all --all --namespace stg-custom-ns
    kubectl delete -f deploy-dev-namespace+Ingress.yaml
    kubectl delete -f deploy-stg-namespace+Ingress.yaml
    
```