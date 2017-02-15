package org.demofx.compare;

import org.demofx.ComparedInfo;
import org.demofx.dto.ProcessesDto;

import java.util.List;

public interface ProcessCompare {
    List<ComparedInfo> compareProcesses(List<ProcessesDto> currentProcesses, List<ProcessesDto> importedProcesses);
}
