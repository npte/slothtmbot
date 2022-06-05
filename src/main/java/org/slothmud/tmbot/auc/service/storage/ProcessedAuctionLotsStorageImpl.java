package org.slothmud.tmbot.auc.service.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slothmud.tmbot.auc.model.AucLot;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProcessedAuctionLotsStorageImpl implements ProcessedAuctionLotsStorage {

    @JsonSerialize(keyUsing = KeySerializer.class)
    private static final Map<Key, Instant> STORAGE = new ConcurrentHashMap<>();
    private static final Object LOCK = new Object();
    private static final TypeReference<Map<Key, Instant>> TYPE_REFERENCE = new TypeReference<>() { };
    static final String STORAGE_FILE_NAME = "auclist.storage";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.registerModule(new JavaTimeModule());

        SimpleModule module = new SimpleModule();
        module.addKeySerializer(Key.class, new KeySerializer());
        module.addKeyDeserializer(Key.class, new com.fasterxml.jackson.databind.KeyDeserializer() {
            @Override
            public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
                return MAPPER.readValue(s, Key.class);
            }
        });
        MAPPER.registerModule(module);
    }

    private final Function<String, Map<Key, Instant>> readStorageContent = s -> {
        try {
            return MAPPER.readValue(s, TYPE_REFERENCE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    @Override
    public List<AucLot> filterProcessedAuctionLots(List<AucLot> unfiltered) {
        STORAGE.entrySet().removeIf(entry ->
                Period.between(LocalDate.now(), entry.getValue().atZone(ZoneId.systemDefault()).toLocalDate()).get(ChronoUnit.DAYS) > 10);

        Map<Key, AucLot> currentLots = unfiltered.stream()
                .collect(Collectors.toMap(
                        e -> new Key(e.getSlot(), e.getSeller()),
                        Function.identity()
                ));

        STORAGE.entrySet().removeIf(entry -> !currentLots.containsKey(entry.getKey()));

        currentLots.entrySet().removeIf(entry -> STORAGE.containsKey(entry.getKey()));

        STORAGE.putAll(
                currentLots.keySet().stream().collect(Collectors.toMap(
                        Function.identity(),
                        e -> Instant.now()
                ))
        );

        flush();

        return new ArrayList<>(currentLots.values());
    }

    @PostConstruct
    public void init() {
        Path path = Paths.get(STORAGE_FILE_NAME);
        if (Files.exists(path)) {
            String content = readAll(path);
            Optional.ofNullable(content)
                    .map(readStorageContent)
                    .ifPresent(STORAGE::putAll);
        }
    }

    @SneakyThrows
    private void flush() {
        synchronized (LOCK) {
            File file = new File(STORAGE_FILE_NAME);
            MAPPER.writeValue(file, STORAGE);
        }
    }

    private String readAll(Path path) {
        try {
            return Files.readAllLines(path).get(0);
        } catch (IOException e) {
            return null;
        }
    }

    @Getter
    @EqualsAndHashCode
    private static class Key {
        private final String slot;
        private final String seller;

        @JsonCreator
        public Key(@JsonProperty("slot") String slot, @JsonProperty("seller") String seller) {
            this.slot = slot;
            this.seller = seller;
        }
    }

    private static class KeySerializer extends JsonSerializer<Key> {
        @Override
        public void serialize(Key value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            StringWriter writer = new StringWriter();
            MAPPER.writeValue(writer, value);
            gen.writeFieldName(writer.toString());
        }
    }

}
