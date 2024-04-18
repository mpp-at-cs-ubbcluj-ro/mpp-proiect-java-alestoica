package utils;

import protobufprotocol.ProtoClientWorker;
import rpcprotocol.ClientWorker;
import service.IService;

import java.net.Socket;

public class ProtoConcurrentServer extends AbstractConcurrentServer {
    private final IService server;

    public ProtoConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoClientWorker worker = new ProtoClientWorker(server, client);
        return new Thread(worker);
    }
}
