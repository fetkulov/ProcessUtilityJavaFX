package org.demofx;

import org.demofx.dto.ProcessesDto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


class GroupUtil {

    static List<ProcessesDto> groupByName(List<OsProcess> osProcesses) {
        Map<String, Double> grouped =
                osProcesses.stream()
                        .collect(Collectors.groupingBy(OsProcess::getImageName,
                                Collectors.summingDouble(OsProcess::getMemoryUsedKB)));
        List<ProcessesDto> processesDtoList = grouped.entrySet().stream()
                .map(e -> new ProcessesDto(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        Comparator<ProcessesDto> comparator = Comparator.comparing(ProcessesDto::getMemory);
        processesDtoList.sort(comparator.reversed());
        return processesDtoList;
    }
}
