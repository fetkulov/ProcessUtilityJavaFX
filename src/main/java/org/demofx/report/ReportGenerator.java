package org.demofx.report;

import org.demofx.dto.ProcessesDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public interface ReportGenerator {

    /**
     * Generating report into file
     *
     * @param processesDtos list of processes
     * @param filePath filename
     */
    void generateReport(List<ProcessesDto> processesDtos, String filePath) throws IOException;
}
