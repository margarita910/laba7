package laba.commands.general;

import laba.sercurity.User;

public interface CommandWithRightsNeeded extends Command {
    void setUser(User user);
}
