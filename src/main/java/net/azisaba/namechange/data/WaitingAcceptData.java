package net.azisaba.namechange.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class WaitingAcceptData {

    private final DataFiles files;
    private final UUID authorUUID;
    private final String authorName;
    private final String previousID, newID;
    private final int customModelData;
    private UUID approverUUID;
    private String approverName;

    private boolean completed;
    @Setter
    private boolean ableToDelete = false;

    public void setCompleted() {
        completed = true;
        if (ableToDelete && files.getWaitingAcceptFile().exists() && !files.getWaitingAcceptFile().delete()) {
            Bukkit.getLogger().warning("Failed to delete file. (" + files.getWaitingAcceptFile().getAbsolutePath() + ")");
        }
    }

    public void setApprover(Player approver) {
        approverUUID = approver.getUniqueId();
        approverName = approver.getName();
    }
}
