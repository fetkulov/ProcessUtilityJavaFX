package org.demofx.tasklist.impl;

import org.demofx.OsProcess;
import org.demofx.tasklist.TaskListing;

import javax.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Named
public class WindowsTaskListing implements TaskListing {

    private static final String CODE_PAGE_437 = "Active code page: 437";

    /**
     * Setting Active code page: 437 by "chcp 437" command
     * Getting OS processes info by "tasklist /nh /FO CSV" command
     *
     * @return List with OS processes information
     */

    @Override
    public List<OsProcess> getRunningTaskInfo() {
        ArrayList<OsProcess> osProcesses = new ArrayList<>();
        try {
            String line;
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "chcp 437 && tasklist /nh /FO CSV");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader inputProc =
                    new BufferedReader(new InputStreamReader(process.getInputStream(), "Cp437"));
            //another possible solution http://bit.ly/2lJHpby
            String activeCodePage = inputProc.readLine();
            //throwing error if cannot set 437 codepage
            while ((line = inputProc.readLine()) != null) {

                String[] processDetail = line.split("\",\"");

                NumberFormat format = NumberFormat.getInstance(Locale.US);
                Number number = format.parse(processDetail[4].substring(0, processDetail[4].length() - 3));
                Double memUsed = number.doubleValue();

                OsProcess osProcess = new OsProcess(processDetail[0].substring(1), processDetail[1], memUsed);
                osProcesses.add(osProcess);

            }
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        return osProcesses.stream()
                .sorted(Comparator.comparing(OsProcess::getMemoryUsedKB).reversed())
                .collect(Collectors.toList());
    }
}
