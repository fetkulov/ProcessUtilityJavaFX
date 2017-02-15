package org.demofx.dto;


import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"name", "memory"})
public class ProcessesDto {

    private String name;
    private Double memory;

    public ProcessesDto() {
    }

    public ProcessesDto(String name, Double memory) {
        this.name = name;
        this.memory = memory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }
}
