package net.azisaba.namechange.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class DataFiles {

    private final File waitingAcceptFile, crackShotFile, crackShotPlusFile, gunScopeRecoilFile;

}
