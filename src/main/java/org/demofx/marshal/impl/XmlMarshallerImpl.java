package org.demofx.marshal.impl;

import org.demofx.dto.TasksDto;
import org.demofx.marshal.Marshaller;

import javax.inject.Named;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@Named
public class XmlMarshallerImpl implements Marshaller {

    @Override
    public void marshal(TasksDto tasksDto, String fileName) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(TasksDto.class);
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(tasksDto, new FileWriter(fileName));

    }

    @Override
    public TasksDto unmarshal(String fileName) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(TasksDto.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (TasksDto) jaxbUnmarshaller.unmarshal(new File(fileName));

    }
}
