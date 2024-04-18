package protobufprotocol;

import model.*;
import rpcprotocol.*;
import service.IObserver;
import service.IService;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ProtoProxy implements IService {
    private final String host;
    private final int port;
    private IObserver client;
    private InputStream input;
    private OutputStream output;
    private Socket connection;
    private final BlockingQueue<Protobufs.Response> responses;
    private volatile boolean finished;

    public ProtoProxy(String host, int port) {
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
            output = connection.getOutputStream();
            input = connection.getInputStream();
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

    private Protobufs.Response readResponse() {
        Protobufs.Response response = null;

        try{
            response = responses.take();
            System.out.println("readResponse: " + response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void sendRequest(Protobufs.Request request) {
        try {
            System.out.println("sendRequest " + request);
            request.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee login(Employee employee, IObserver client) throws Exception {
        initializeConnection();

        Protobufs.Request request = ProtoUtils.createLoginRequest(employee);
        sendRequest(request);

        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.OK) {
            this.client = client;
            return ProtoUtils.getEmployee(response.getEmployee());
        } else if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println("Error " + error);
            closeConnection();
            throw new Exception(error);
        }

        return null;
    }

    @Override
    public void logout(Long id, IObserver client) throws Exception {
        Protobufs.Request request = ProtoUtils.createLogoutRequest(id);
        sendRequest(request);

        Protobufs.Response response = readResponse();
        closeConnection();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            throw new Exception(error);
        }
    }

    @Override
    public Employee findOneByUsernameAndPassword(String username, String password) {
        Protobufs.Request request = ProtoUtils.createGetEmployeeRequest();
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }

        return ProtoUtils.getEmployee(response.getEmployee());
    }

    @Override
    public Collection<AgeEvent> getAllAgeEvents() {
        Protobufs.Request request = ProtoUtils.createGetEventsRequest();
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }

        return ProtoUtils.getAgeEvents(response.getAgeEventsList());
    }

    @Override
    public Collection<Participant> getAllParticipants() {
        Protobufs.Request request = ProtoUtils.createGetParticipantsRequest();
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }

        return ProtoUtils.getParticipants(response.getParticipantsList());
    }

    @Override
    public Collection<Registration> findByAgeEvent(AgeEvent ageEvent) {
        Protobufs.Request request = ProtoUtils.createGetRegistrationByAgeEventRequest(ageEvent);
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }

        return ProtoUtils.getRegistrations(response.getRegistrationsList());
    }

    @Override
    public Collection<Registration> findByParticipant(Participant participant) {
        Protobufs.Request request = ProtoUtils.createGetRegistrationByPartcicpantRequest(participant);
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
            return null;
        }
        else
            return ProtoUtils.getRegistrations(response.getRegistrationsList());
    }

    @Override
    public Participant findOne(long id) {
        Protobufs.Request request = ProtoUtils.createGetParticipantRequest(id);
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }

        return ProtoUtils.getParticipant(response.getParticipant());
    }

    @Override
    public Participant findOneByNameAndAge(String firstName, String lastName, int age) {
        Participant participant = new Participant(firstName, lastName, age);
        participant.setId(1L);
        Protobufs.Request request = ProtoUtils.createGetParticipantByNameAndAgeRequest(participant);
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }

        return ProtoUtils.getParticipant(response.getParticipant());
    }

    @Override
    public AgeEvent findByAgeGroupAndSportsEvent(String ageGroup, String sportsEvent) {
        AgeEvent ageEvent = new AgeEvent(AgeGroup.valueOf(ageGroup), SportsEvent.valueOf(sportsEvent));
        ageEvent.setId(1L);
        Protobufs.Request request = ProtoUtils.createGetAgeEventRequest(ageEvent);
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }

        return ProtoUtils.getAgeEvent(response.getAgeEvent());
    }

    @Override
    public void addRegistration(Registration entity) {
        Protobufs.Request request = ProtoUtils.createAddRegistrationRequest(entity);
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }
    }

    @Override
    public void addParticipant(Participant entity) {
        Protobufs.Request request = ProtoUtils.createAddParticipantRequest(entity);
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }
    }

    @Override
    public int countParticipants(AgeEvent ageEvent) {
        Protobufs.Request request = ProtoUtils.createCountParticipantsRequest(ageEvent);
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }

        return response.getCount();
    }

    @Override
    public int countRegistrations(Participant participant) {
        Protobufs.Request request = ProtoUtils.createCountRegistrationsRequest(participant);
        sendRequest(request);
        Protobufs.Response response = readResponse();

        if (response.getType() == Protobufs.Response.ResponseType.ERROR) {
            String error = ProtoUtils.getError(response);
            System.out.println(error);
        }

        return response.getCount();
    }

    private boolean isUpdate(Protobufs.Response response){
        return response.getType() == Protobufs.Response.ResponseType.NEW_REGISTRATION;
//        || response.type() == ResponseType.NEW_PARTICIPANT;
    }

    private void handleUpdate(Protobufs.Response response) throws Exception {
        if (response.getType() == Protobufs.Response.ResponseType.NEW_REGISTRATION){
            Protobufs.Registration registration = response.getRegistration();

            try {
                System.out.println("notify add registration");
                client.notifyAddRegistration(ProtoUtils.getRegistration(registration));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        else if (response.type() == ResponseType.NEW_PARTICIPANT){
//            Participant participant = (Participant) response.data();
//
//            try {
//                System.out.println("notify add participant");
//                client.notifyAddParticipant(participant);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Protobufs.Response response = Protobufs.Response.parseDelimitedFrom(input);
//                    System.out.println("Proxy response: " + response);

                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        try {
                            responses.put(response);
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
