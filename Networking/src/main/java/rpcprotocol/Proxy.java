package rpcprotocol;

import model.*;
import service.IObserver;
import service.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Proxy implements IService {
    private final String host;
    private final int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private final BlockingQueue<Response> responses;
    private volatile boolean finished;

    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingDeque<>();
    }

    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void initializeConnection() throws Exception {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response readResponse() {
        Response response = null;

        try{
            response = responses.take();
            System.out.println("readResponse: " + response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void sendRequest(Request request) {
        try {
            System.out.println("sendRequest " + request);
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee login(Employee employee, IObserver client) throws Exception {
        initializeConnection();

        Request request = new Request.Builder().type(RequestType.LOGIN).data(employee).build();
        sendRequest(request);

        Response response = readResponse();

        if (response.type() == ResponseType.OK) {
            this.client = client;
            return (Employee) response.data();
        } else if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            System.out.println("Error " + error);
            closeConnection();
            throw new Exception(error);
        }

        return null;
    }

    @Override
    public void logout(Long id, IObserver client) throws Exception {
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(id).build();
        sendRequest(request);

        Response response = readResponse();
        closeConnection();

        if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            throw new Exception(error);
        }
    }

    @Override
    public Employee findOneByUsernameAndPassword(String username, String password) {
        Request request = new Request.Builder().type(RequestType.GET_EMPLOYEE).data(1).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            System.out.println(error);
        }

        return (Employee) response.data();
    }

    @Override
    public Collection<AgeEvent> getAllAgeEvents() {
        Request request = new Request.Builder().type(RequestType.GET_EVENTS).data(1).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            System.out.println(error);
        }

        return (Collection<AgeEvent>) response.data();
    }

    @Override
    public Collection<Participant> getAllParticipants() {
        Request request = new Request.Builder().type(RequestType.GET_PARTICIPANTS).data(1).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            System.out.println(error);
        }

        return (Collection<Participant>) response.data();
    }

    @Override
    public Collection<Registration> findByAgeEvent(AgeEvent ageEvent) {
        Request request = new Request.Builder().type(RequestType.REGISTRATION_BY_AGE_EVENT).data(ageEvent).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            System.out.println(error);
        }

        return (Collection<Registration>) response.data();
    }

    @Override
    public Collection<Registration> findByParticipant(Participant participant) {
        Request request = new Request.Builder().type(RequestType.REGISTRATION_BY_PARTICIPANT).data(participant).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            System.out.println(error);
            return null;
        }
        else
            return (Collection<Registration>) response.data();
    }

    @Override
    public Participant findOne(long id) {
        Request request = new Request.Builder().type(RequestType.GET_PARTICIPANT).data(id).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            System.out.println(error);
        }

        return (Participant) response.data();
    }

    @Override
    public Participant findOneByNameAndAge(String firstName, String lastName, int age) {
        Participant participant = new Participant(firstName, lastName, age);
        Request request = new Request.Builder().type(RequestType.PARTICIPANT_BY_NAME_AND_AGE).data(participant).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            System.out.println(error);
        }

        return (Participant) response.data();
    }

    @Override
    public AgeEvent findByAgeGroupAndSportsEvent(String ageGroup, String sportsEvent) {
        AgeEvent ageEvent = new AgeEvent(AgeGroup.valueOf(ageGroup), SportsEvent.valueOf(sportsEvent));
        Request request = new Request.Builder().type(RequestType.GET_AGE_EVENT).data(ageEvent).build();
        sendRequest(request);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String error = response.data().toString();
            System.out.println(error);
        }

        return (AgeEvent) response.data();
    }

    @Override
    public void addRegistration(Registration entity) {
        Request req = new Request.Builder().type(RequestType.ADD_REGISTRATION).data(entity).build();
        sendRequest(req);
        Response response = readResponse();

        if(response.type() == ResponseType.ERROR){
            String error = response.data().toString();
            System.out.println(error);
        }
    }

    @Override
    public void addParticipant(Participant entity) {
        Request req = new Request.Builder().type(RequestType.ADD_PARTICIPANT).data(entity).build();
        sendRequest(req);
        Response response = readResponse();

        if(response.type() == ResponseType.ERROR){
            String error = response.data().toString();
            System.out.println(error);
        }
    }

    @Override
    public int countParticipants(AgeEvent ageEvent) {
        Request req = new Request.Builder().type(RequestType.COUNT_PARTICIPANTS).data(ageEvent).build();
        sendRequest(req);
        Response response = readResponse();

        if(response.type() == ResponseType.ERROR){
            String error = response.data().toString();
            System.out.println(error);
        }

        return (int) response.data();
    }

    @Override
    public int countRegistrations(Participant participant) {
        Request req = new Request.Builder().type(RequestType.COUNT_REGISTRATIONS).data(participant).build();
        sendRequest(req);
        Response response = readResponse();

        if(response.type() == ResponseType.ERROR){
            String error = response.data().toString();
            System.out.println(error);
        }

        return (int) response.data();
    }

    private boolean isUpdate(Response response){
        return response.type() == ResponseType.NEW_REGISTRATION || response.type() == ResponseType.NEW_PARTICIPANT;
    }

    private void handleUpdate(Response response) throws Exception {
        if (response.type() == ResponseType.NEW_REGISTRATION){
            Registration registration = (Registration) response.data();

            try {
                System.out.println("notify add registration");
                client.notifyAddRegistration(registration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (response.type() == ResponseType.NEW_PARTICIPANT){
            Participant participant = (Participant) response.data();

            try {
                System.out.println("notify add participant");
                client.notifyAddParticipant(participant);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("Proxy response: " + response);

                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {
                        try {
                            responses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Reading error " + e);
                }
            }
        }
    }
}
