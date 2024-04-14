package rpcprotocol;

import model.*;
import service.IObserver;
import service.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;

import static rpcprotocol.ResponseType.*;

public class ClientWorker implements Runnable, IObserver {
    private final IService service;
    private final Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientWorker(IService service, Socket connection) {
        this.service = service;
        this.connection = connection;

        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();

                System.out.println("run request " + request);

                Response response = handleRequest((Request) request);

                if (response != null) {
                    System.out.println("run response" + response);
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private Response handleRequest (Request request) {
        Response response = null;

        if (request.type() == RequestType.LOGIN) {
            Employee employee = (Employee) request.data();

            try {
                Employee foundEmployee = service.login(employee, this);
                return new Response.Builder().type(OK).data(foundEmployee).build();
            } catch (Exception e) {
                connected = false;
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.LOGOUT) {
            Long employeeId = (Long) request.data();

            try {
                service.logout(employeeId, this);
                connected = false;
                return new Response.Builder().type(OK).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.ADD_REGISTRATION) {
            Registration registration = (Registration) request.data();

            try {
                service.addRegistration(registration);
                return new Response.Builder().type(OK).data(registration).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.ADD_PARTICIPANT) {
            Participant participant = (Participant) request.data();

            try {
                service.addParticipant(participant);
                return new Response.Builder().type(OK).data(participant).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.GET_EVENTS) {
            try {
                Collection<AgeEvent> ageEvents = service.getAllAgeEvents();
                return new Response.Builder().type(GET_EVENTS).data(ageEvents).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.GET_PARTICIPANTS) {
            try {
                Collection<Participant> participants = service.getAllParticipants();
                return new Response.Builder().type(GET_PARTICIPANTS).data(participants).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.GET_PARTICIPANT) {
            Long participantId = (Long) request.data();

            try {
                Participant foundParticipant = service.findOne(participantId);
                return new Response.Builder().type(OK).data(foundParticipant).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.GET_EMPLOYEE) {
            Employee employee = (Employee) request.data();

            try {
                Employee foundEmployee = service.findOneByUsernameAndPassword(employee.getUsername(), employee.getPassword());
                return new Response.Builder().type(OK).data(foundEmployee).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.GET_AGE_EVENT) {
            AgeEvent ageEvent = (AgeEvent) request.data();

            try {
                AgeEvent foundAgeEvent = service.findByAgeGroupAndSportsEvent(ageEvent.getAgeGroup().toString(), ageEvent.getSportsEvent().toString());
                return new Response.Builder().type(OK).data(foundAgeEvent).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.REGISTRATION_BY_PARTICIPANT) {
            Participant participant = (Participant) request.data();

            try {
                Collection<Registration> registrations = service.findByParticipant(participant);
                return new Response.Builder().type(OK).data(registrations).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.REGISTRATION_BY_AGE_EVENT) {
            AgeEvent ageEvent = (AgeEvent) request.data();

            try {
                Collection<Registration> registrations = service.findByAgeEvent(ageEvent);
                return new Response.Builder().type(OK).data(registrations).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.PARTICIPANT_BY_NAME_AND_AGE) {
            Participant participant = (Participant) request.data();

            try {
                Participant foundParticipant = service.findOneByNameAndAge(participant.getFirstName(), participant.getLastName(), participant.getAge());
                return new Response.Builder().type(OK).data(foundParticipant).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.COUNT_PARTICIPANTS) {
            AgeEvent ageEvent = (AgeEvent) request.data();

            try {
                int count = service.countParticipants(ageEvent);
                return new Response.Builder().type(OK).data(count).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.COUNT_REGISTRATIONS) {
            Participant participant = (Participant) request.data();

            try {
                int count = service.countRegistrations(participant);
                return new Response.Builder().type(OK).data(count).build();
            } catch (Exception e) {
                return new Response.Builder().type(ERROR).data(e.getMessage()).build();
            }
        } else
            return response;
    }

    private void sendResponse(Response resp) throws IOException {
        synchronized (output) {
            System.out.println("sendResponse " + resp);
            output.writeObject(resp);
            output.flush();
        }
    }

    @Override
    public void notifyAddRegistration(Registration registration) throws Exception {
        Response response = new Response.Builder().type(ResponseType.NEW_REGISTRATION).data(registration).build();
        System.out.println("notify registration in client worker");

        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new Exception("Adding registration error: " + e.getMessage());
        }
    }

    @Override
    public void notifyAddParticipant(Participant participant) throws Exception {
        Response response = new Response.Builder().type(ResponseType.NEW_PARTICIPANT).data(participant).build();
        System.out.println("notify participant in client worker");

        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new Exception("Adding participant error: " + e.getMessage());
        }
    }
}
