# ZIO Akka


## Hey benchmark

```bash
hey -n 200 -c 8 -z 30s -o csv http://127.0.0.1:8000/job/100000 | go run hey-hdr.go -out zio
```


