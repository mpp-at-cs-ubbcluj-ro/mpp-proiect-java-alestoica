package protobufprotocol;

import model.AgeEvent;
import model.Employee;
import model.Participant;
import model.Registration;
import rpcprotocol.Request;
import rpcprotocol.RequestType;
import rpcprotocol.Response;
import rpcprotocol.ResponseType;
import service.IObserver;
import service.IService;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

import static rpcprotocol.ResponseType.*;
import static rpcprotocol.ResponseType.ERROR;

public class ProtoClientWorker implements Runnable, IObserver {
    private final IService service;
    private final Socket connection;
    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public ProtoClientWorker(IService service, Socket connection) {
        this.service = service;
        this.connection = connection;

        try {
            output = connection.getOutputStream();
            input = connection.getInputStream();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Protobufs.Request request = Protobufs.Request.parseDelimitedFrom(input);

                System.out.println("run request " + request);

                Protobufs.Response response = handleRequest(request);

                if (response != null) {
                    System.out.println("run response" + response);
                    sendResponse(response);
                }
            } catch (IOException e) {
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

    private Protobufs.Response handleRequest (Protobufs.Request request) {
        Protobufs.Response response = null;

        if (request.getType() == Protobufs.Request.RequestType.LOGIN) {
            Employee employee = ProtoUtils.getEmployee(request.getEmployee());

            try {
                Employee foundEmployee = service.login(employee, this);
                return ProtoUtils.setEmployee(ProtoUtils.createOkResponse(), foundEmployee);
            } catch (Exception e) {
                connected = false;
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.LOGOUT) {
            Long employeeId = request.getId();

            try {
                service.logout(employeeId, this);
                connected = false;
                return ProtoUtils.createOkResponse().build();
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.ADD_REGISTRATION) {
            Registration registration = ProtoUtils.getRegistration(request.getRegistration());

            try {
                service.addRegistration(registration);
                return ProtoUtils.setRegistration(ProtoUtils.createOkResponse(), registration);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.ADD_PARTICIPANT) {
            Participant participant = ProtoUtils.getParticipant(request.getParticipant());

            try {
                service.addParticipant(participant);
                return ProtoUtils.setParticipant(ProtoUtils.createOkResponse(), participant);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.GET_EVENTS) {
            try {
                Collection<AgeEvent> ageEvents = service.getAllAgeEvents();
                return ProtoUtils.createGetEventsResponse(ageEvents);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.GET_PARTICIPANTS) {
            try {
                Collection<Participant> participants = service.getAllParticipants();
                return ProtoUtils.createGetParticipantsResponse(participants);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.GET_PARTICIPANT) {
            long participantId = request.getId();

            try {
                Participant foundParticipant = service.findOne(participantId);
                return ProtoUtils.setParticipant(ProtoUtils.createOkResponse(), foundParticipant);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.GET_EMPLOYEE) {
            Employee employee = ProtoUtils.getEmployee(request.getEmployee());

            try {
                Employee foundEmployee = service.findOneByUsernameAndPassword(employee.getUsername(), employee.getPassword());
                return ProtoUtils.setEmployee(ProtoUtils.createOkResponse(), foundEmployee);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.GET_AGE_EVENT) {
            AgeEvent ageEvent = ProtoUtils.getAgeEvent(request.getAgeEvent());

            try {
                AgeEvent foundAgeEvent = service.findByAgeGroupAndSportsEvent(ageEvent.getAgeGroup().toString(), ageEvent.getSportsEvent().toString());
                return ProtoUtils.setAgeEvent(ProtoUtils.createOkResponse(), foundAgeEvent);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.REGISTRATION_BY_PARTICIPANT) {
            Participant participant = ProtoUtils.getParticipant(request.getParticipant());

            try {
                Collection<Registration> registrations = service.findByParticipant(participant);
                return ProtoUtils.setRegistrations(ProtoUtils.createOkResponse(), registrations);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.REGISTRATION_BY_AGE_EVENT) {
            AgeEvent ageEvent = ProtoUtils.getAgeEvent(request.getAgeEvent());

            try {
                Collection<Registration> registrations = service.findByAgeEvent(ageEvent);
                return ProtoUtils.setRegistrations(ProtoUtils.createOkResponse(), registrations);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.PARTICIPANT_BY_NAME_AND_AGE) {
            Participant participant = ProtoUtils.getParticipant(request.getParticipant());

            try {
                Participant foundParticipant = service.findOneByNameAndAge(participant.getFirstName(), participant.getLastName(), participant.getAge());
                return ProtoUtils.setParticipant(ProtoUtils.createOkResponse(), foundParticipant);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.COUNT_PARTICIPANTS) {
            AgeEvent ageEvent = ProtoUtils.getAgeEvent(request.getAgeEvent());

            try {
                int count = service.countParticipants(ageEvent);
                return ProtoUtils.setCount(ProtoUtils.createOkResponse(), count);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else if (request.getType() == Protobufs.Request.RequestType.COUNT_REGISTRATIONS) {
            Participant participant = ProtoUtils.getParticipant(request.getParticipant());

            try {
                int count = service.countRegistrations(participant);
                return ProtoUtils.setCount(ProtoUtils.createOkResponse(), count);
            } catch (Exception e) {
                return ProtoUtils.createErrorResponse(e.getMessage());
            }
        } else
            return response;
    }

    private void sendResponse(Protobufs.Response response) throws IOException {
        synchronized (output) {
            System.out.println("sendResponse " + response);
            response.writeDelimitedTo(output);
            output.flush();
        }
    }

    @Override
    public void notifyAddRegistration(Registration registration) throws Exception {
        System.out.println("notify registration in client worker");

        try {
            sendResponse(ProtoUtils.createNewRegistrationResponse(registration));
        } catch (IOException e) {
            throw new Exception("Adding registration error: " + e.getMessage());
        }
    }

//    @Override
//    public void notifyAddParticipant(Participant participant) throws Exception {
//        Response response = new Response.Builder().type(ResponseType.NEW_PARTICIPANT).data(participant).build();
//        System.out.println("notify participant in client worker");
//
//        try {
//            sendResponse(response);
//        } catch (IOException e) {
//            throw new Exception("Adding participant error: " + e.getMessage());
//        }
//    }
}
