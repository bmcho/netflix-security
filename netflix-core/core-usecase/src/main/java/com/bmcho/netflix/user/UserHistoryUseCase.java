package com.bmcho.netflix.user;

import com.bmcho.netflix.user.command.UserHistoryCommand;

public interface UserHistoryUseCase {
    void createHistory(UserHistoryCommand userHistoryCommand);
}
