package org.slothmud.tmbot.party.service.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slothmud.tmbot.party.model.PartyInfo;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProcessedPartiesStorageImpl implements ProcessedPartiesStorage {

    private static final List<PartyInfo> STORAGE = Collections.synchronizedList(new ArrayList<>());
    private static final Object LOCK = new Object();
    private static final TypeReference<List<PartyInfo>> TYPE_REFERENCE = new TypeReference<>() {};
    static final String STORAGE_FILE_NAME = "parties.storage";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public List<PartyInfo> filterProcessedParties(List<PartyInfo> unfiltered) {
        STORAGE.removeIf(partyInfo -> !unfiltered.contains(partyInfo));

        List<PartyInfo> response = unfiltered.stream()
                .filter(partyInfo -> !STORAGE.contains(partyInfo))
                .collect(Collectors.toList());

        STORAGE.addAll(response);

        flush();

        return response;
    }

    @PostConstruct
    public void init() {
        Path path = Paths.get(STORAGE_FILE_NAME);
        if (Files.exists(path)) {
            String content = readAll(path);
            Optional.ofNullable(content)
                    .map(readStorageContent)
                    .ifPresent(STORAGE::addAll);
        }
    }

    @SneakyThrows
    private void flush() {
        synchronized (LOCK) {
            File file = new File(STORAGE_FILE_NAME);
            MAPPER.writeValue(file, STORAGE);
        }
    }

    private final Function<String, List<PartyInfo>> readStorageContent = s -> {
        try {
            return MAPPER.readValue(s, TYPE_REFERENCE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    private String readAll(Path path) {
        try {
            return Files.readAllLines(path).get(0);
        } catch (IOException e) {
            return null;
        }
    }

}
