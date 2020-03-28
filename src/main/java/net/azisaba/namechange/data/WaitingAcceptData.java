package net.azisaba.namechange.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class WaitingAcceptData {

    private final DataFiles files;
    private final UUID authorUUID;
    private final String authorName;
    private final String previousID, newID;

    private boolean completed;
    @Setter
    private boolean ableToDelete = false;

    public void setCompleted() {
        completed = true;
        if (ableToDelete && files.getWaitingAcceptFile().exists() && !files.getWaitingAcceptFile().delete()) {
            Bukkit.getLogger().warning("Failed to delete file. (" + files.getWaitingAcceptFile().getAbsolutePath() + ")");
        }
    }
}
