package idwall.desafio.crawler.rpc.server;

import com.google.protobuf.Service;

public abstract class RpcServer implements Runnable {

    public RpcServer(Service service) {
        this.service = service;
    }

    public Service getService() {
        return this.service;
    }

    private Service service;

}
