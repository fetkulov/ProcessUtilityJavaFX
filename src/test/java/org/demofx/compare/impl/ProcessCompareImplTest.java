package org.demofx.compare.impl;

import org.demofx.ComparedInfo;
import org.demofx.compare.ProcessCompare;
import org.demofx.dto.ProcessesDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ildar.fetkulov on 2/14/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ProcessCompareImplTest {

    @Inject
    ProcessCompare processCompare;

    @Test
    public void compareProcesses() throws Exception {
        List<ProcessesDto> currentProcessesDtos = new ArrayList<>();
        List<ProcessesDto> importedProcessesDtos = new ArrayList<>();

        ProcessesDto processesDtoCur1 = new ProcessesDto();
        processesDtoCur1.setMemory(100d);
        processesDtoCur1.setName("idle");
        ProcessesDto processesDtoCur2 = new ProcessesDto();
        processesDtoCur2.setMemory(1200d);
        processesDtoCur2.setName("word.exe");
        ProcessesDto processesDtoCur3 = new ProcessesDto();
        processesDtoCur3.setMemory(300000d);
        processesDtoCur3.setName("java.exe");
        currentProcessesDtos.addAll(Arrays.asList(processesDtoCur1, processesDtoCur2, processesDtoCur3));


        ProcessesDto processesDtoImp1 = new ProcessesDto();
        processesDtoImp1.setMemory(100d);
        processesDtoImp1.setName("idle");
        ProcessesDto processesDtoImp3 = new ProcessesDto();
        processesDtoImp3.setMemory(200000d);
        processesDtoImp3.setName("java.exe");
        importedProcessesDtos.addAll(Arrays.asList(processesDtoImp1, processesDtoImp3));


        List<ComparedInfo> comparedInfos = processCompare.compareProcesses(currentProcessesDtos, importedProcessesDtos);
        assertTrue(comparedInfos.size() == 3);
        // difference for top memory consuming processes
        assertTrue(comparedInfos.get(0).isLeftRed());
        assertTrue(comparedInfos.get(0).isRightRed());
    }

}
