package org.demofx.tasklist;

import org.demofx.OsProcess;

import java.util.List;


public interface TaskListing {

    List<OsProcess> getRunningTaskInfo();
}
