package net.azisaba.namechange.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class FactoryResponse {

    private final FactoryStatus status;
    private final File file;

    public enum FactoryStatus {
        SUCCESS, FAIL, NO_NEED
    }
}
