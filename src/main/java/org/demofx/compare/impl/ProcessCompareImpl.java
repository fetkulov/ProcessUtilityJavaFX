package org.demofx.compare.impl;

import org.demofx.ComparedInfo;
import org.demofx.compare.ProcessCompare;
import org.demofx.dto.ProcessesDto;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named
public class ProcessCompareImpl implements ProcessCompare {

    /**
     * compare two reduced "process name-consumed memory" lists
     * and create ComparedInfo with blank places if absent
     * and marking different with rightRed/leftRed
     */
    @Override
    public List<ComparedInfo> compareProcesses(List<ProcessesDto> currentProcesses, List<ProcessesDto> importedProcesses) {

        Map<String, Double> currentNameToMemory = currentProcesses.stream()
                .collect(
                        Collectors.toMap(ProcessesDto::getName, ProcessesDto::getMemory));
        Map<String, Double> importedNameToMemory = importedProcesses.stream()
                .collect(
                        Collectors.toMap(ProcessesDto::getName, ProcessesDto::getMemory));
        List<String> mergedTaskNames = mergeTaskNames(currentProcesses, importedProcesses);

        //creating Map with max consumed memory value
        Map<String, Double> mergedNameToMemory = mergedTaskNames.stream()
                .collect(Collectors.toMap(name -> name,
                        name -> {
                            Double currentMemory = currentNameToMemory.get(name);
                            Double importedMemory = importedNameToMemory.get(name);

                            if (currentMemory != null && importedMemory != null) {
                                return currentMemory > importedMemory ? currentMemory : importedMemory;
                            } else if (currentMemory != null) {
                                return currentMemory;
                            } else {
                                return importedMemory;
                            }
                        }));

        //sorting Map
        Map<String, Double> sortedMergedNameMemory = new LinkedHashMap<>();

        mergedNameToMemory.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEachOrdered(x -> sortedMergedNameMemory.put(x.getKey(), x.getValue()));

        List<ComparedInfo> comparedInfos = new ArrayList<>();

        //filling ComparedInfo
        sortedMergedNameMemory.forEach((name, memory) -> {

            Double currentMemory = currentNameToMemory.get(name);
            Double importedMemory = importedNameToMemory.get(name);
            ComparedInfo comparedInfo = new ComparedInfo();

            if (currentMemory != null && importedMemory != null) {
                comparedInfo.setCurrentProcessName(name);
                comparedInfo.setCurrentProcessMemory(currentMemory);
                comparedInfo.setImportedProcessName(name);
                comparedInfo.setImportedProcessMemory(importedMemory);
                if (!currentMemory.equals(importedMemory)) {
                    comparedInfo.setLeftRed(true);
                    comparedInfo.setRightRed(true);
                }
            } else if (currentMemory != null) {
                comparedInfo.setCurrentProcessName(name);
                comparedInfo.setCurrentProcessMemory(currentMemory);
            } else {
                comparedInfo.setImportedProcessName(name);
                comparedInfo.setImportedProcessMemory(importedMemory);
            }
            comparedInfos.add(comparedInfo);
        });

        return comparedInfos;
    }

    private List<String> mergeTaskNames(List<ProcessesDto> currentProcesses, List<ProcessesDto> importedProcesses) {

        List<String> currentProcessNames = currentProcesses.stream()
                .map(ProcessesDto::getName)
                .collect(Collectors.toList());
        List<String> importedProcessNames = importedProcesses.stream()
                .map(ProcessesDto::getName)
                .collect(Collectors.toList());
        return Stream.concat(currentProcessNames.stream(), importedProcessNames.stream())
                .distinct()
                .collect(Collectors.toList());

    }
}
