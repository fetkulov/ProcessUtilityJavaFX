package org.demofx.marshal;

import org.demofx.dto.TasksDto;

import javax.xml.bind.JAXBException;
import java.io.IOException;


public interface Marshaller {

    void marshal(TasksDto tasksDto, String fileName) throws JAXBException, IOException;

    TasksDto unmarshal(String fileName) throws JAXBException;
}
