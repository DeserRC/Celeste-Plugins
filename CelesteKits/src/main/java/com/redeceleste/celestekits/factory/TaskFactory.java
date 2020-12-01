package com.redeceleste.celestekits.factory;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.task.UserGetTask;
import com.redeceleste.celestekits.task.UserUpdateTask;
import lombok.Getter;

@Getter
public class TaskFactory {
    private final CelesteKit main;
    private final UserUpdateTask updateTaskAsync;
    private final UserUpdateTask updateTaskSync;
    private final UserGetTask getTask;

    public TaskFactory(CelesteKit main) {
        this.main = main;
        this.updateTaskAsync = new UserUpdateTask(main, true);
        this.updateTaskSync = new UserUpdateTask(main, false);
        this.getTask = new UserGetTask(main);
    }
}
