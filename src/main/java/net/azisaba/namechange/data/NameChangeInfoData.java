package net.azisaba.namechange.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NameChangeInfoData {
    private final String baseWeapon;
    private final String authorName;
    private final String authorUUID;
    private final String approverName;
    private final String approverUUID;
    private final int customModelData;
}
