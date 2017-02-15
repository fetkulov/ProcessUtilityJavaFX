package org.demofx;

import org.demofx.dto.ProcessesDto;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static org.junit.Assert.*;

/**
 * Created by ildar.fetkulov on 2/14/2017.
 */
public class GroupUtilTest {
    @Test
    public void groupByName() throws Exception {
        OsProcess osProcess1 = new OsProcess("Idle", "111", 10d);
        OsProcess osProcess2 = new OsProcess("Java", "711", 30000d);
        OsProcess osProcess3 = new OsProcess("Java", "712", 35000d);
        List<ProcessesDto> processesAggregated = GroupUtil.groupByName(new ArrayList<OsProcess>(Arrays.asList(osProcess1, osProcess2, osProcess3)));
        assertEquals(2, processesAggregated.size());
        ProcessesDto processJava = processesAggregated.stream().filter(p -> p.getName().equals("Java")).findFirst().get();
        assertTrue(abs(65000d - processJava.getMemory()) < 1d);
    }

}