package me.tiredy.sleepy.data.schema;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@SuppressWarnings("unused")
@DatabaseTable(tableName = "sleepydata")
public class SchemaBasic {
    public static final String UUID_FIELD = "schemaUUID";
    @DatabaseField
    private final UUID schemaUUID;

    public SchemaBasic(UUID uuid) {
        this.schemaUUID = uuid;
    }

    public UUID getUUID() {
        return schemaUUID;
    }
}
