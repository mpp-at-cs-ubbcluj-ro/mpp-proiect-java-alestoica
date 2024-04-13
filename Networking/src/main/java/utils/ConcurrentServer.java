package utils;

import rpcprotocol.ClientWorker;
import service.IService;

import java.net.Socket;

public class ConcurrentServer extends AbstractConcurrentServer {
    private final IService server;

    public ConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientWorker worker = new ClientWorker(server, client);
        return new Thread(worker);
    }
}
