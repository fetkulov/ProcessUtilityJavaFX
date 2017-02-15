package org.demofx.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;



@XmlRootElement(name = "tasks")
@XmlAccessorType(XmlAccessType.FIELD)
public class TasksDto {

    @XmlElement(name="task")
    private List<ProcessesDto> processesDtos;

    public TasksDto() {
    }

    public TasksDto(List<ProcessesDto> processesDtos) {
        this.processesDtos = processesDtos;
    }

    public List<ProcessesDto> getProcessesDtos() {
        return processesDtos;
    }

    public void setProcessesDtos(List<ProcessesDto> processesDtos) {
        this.processesDtos = processesDtos;
    }
}
