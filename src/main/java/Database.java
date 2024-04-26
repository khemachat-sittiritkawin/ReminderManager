import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class Database {

    private static Database instance;
    public static Database getInstance() {
        return instance;
    }

    public static void init() throws Exception {

        //System.out.println("Initialize database");
        if (instance != null) {
            throw new Exception("Database is already initialized!");
        }
        instance = new Database();
    }

    public static void shutdown() {
        instance.cleanup();
    }

    private class UpdateThread extends Thread {
        @Override
        public void run() {
            while (!stopped) {
                for (Reminder r : reminders) {
                    r.setDue(r.getDeadline().isBefore(LocalDateTime.now()));
                }
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private GsonBuilder builder;

    private ObservableList<Reminder> reminders;
    private boolean stopped = false;

    public ObservableList<Reminder> getReminders() {
        return reminders;
    }

    private Database() {
        JsonSerializer<LocalDateTime> localDateTimeSerializer = new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                return localDateTime == null ? null : new JsonPrimitive(localDateTime.format(ProgramConfig.getDateTimeFormatter()));
            }
        };

        JsonDeserializer<LocalDateTime> locaDateTimeDeserializer = new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return jsonElement == null ? null : LocalDateTime.parse(jsonElement.getAsString(), ProgramConfig.getDateTimeFormatter());
            }
        };

        builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, localDateTimeSerializer);
        builder.registerTypeAdapter(LocalDateTime.class, locaDateTimeDeserializer);
        builder.setPrettyPrinting();


        Gson gson = builder.create();

        this.reminders = FXCollections.observableArrayList();

        File saveFile = new File(ProgramConfig.SAVE_FILE_NAME);

        try (Reader reader = new FileReader(saveFile)) {
            List<Reminder> loading;
            Type listType = new TypeToken<List<Reminder>>() {}.getType();
            loading = gson.fromJson(reader, listType);
            reminders.addAll(loading);
        } catch (IOException e) {
            System.out.println("Couldn't find the save file. A new save file will be created when the program stops running.");
        } catch (JsonSyntaxException e) {
            System.out.print("The save file is corrupted! Renaming the file so that it won't interfere with the program's save file system.");
            System.out.println("(And rename the file back to \"" + ProgramConfig.SAVE_FILE_NAME + "\" when you are done fixing it.)");

            String[] split = saveFile.getName().split("[.]");
            String name = split[0];
            String exten = split[1];
            int n = 0;

            String newFileName = name + "-" + n + "." + exten;
            File renamedFile = new File(newFileName);
            while (renamedFile.isFile()) {
                n++;
                newFileName = name + "-" + n + "." + exten;
                renamedFile = new File(newFileName);
            }
            saveFile.renameTo(renamedFile);
        }

        UpdateThread job = this.new UpdateThread();
        job.start();

    }

    private void cleanup() {
        stopped = true;

        Gson gson = builder.create();

        try (Writer writer = new FileWriter(ProgramConfig.SAVE_FILE_NAME)){
            gson.toJson(reminders, writer);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
