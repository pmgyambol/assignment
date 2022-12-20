import java.util.Scanner;
import java.util.Arrays;
import java.util.Optional;
import java.util.InputMismatchException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.EnumUtils;


/*
 * Основния клас за работа с JSON файла
 */
public class OrganizerJSON {

    private JSONObject jsonMainObj;
    private String     filename;

    /*
     * Единствен конструктор инициализиран с името на входния JSON файл
     * Използва се стандартен вход от пакет:  org.json.JSONObject
     */
    public OrganizerJSON(String filename) {
        this.filename = filename;
        try {
            File f = new File(filename);
            if (f.exists()) {
                InputStream is = new FileInputStream(filename);
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                this.jsonMainObj = new JSONObject(jsonTxt);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * Визуализира на терминала моментното съдържание на
     * JSONObject jsonMainObj според изискванията за задачата в точка I
     */
    public void printTreeView() {
        JSONArray projects = this.jsonMainObj.getJSONArray("projects");
        for(int i1 = 0; i1 < projects.length(); i1++) {
            System.out.printf("- %s | ID: %d, Статус: %s |\n",
                projects.getJSONObject(i1).getString("name"),
                projects.getJSONObject(i1).getInt("id"),
                projects.getJSONObject(i1).getString("status")
            );
            JSONArray milestones = projects.getJSONObject(i1).getJSONArray("milestones");
            for(int i2 = 0; i2 < milestones.length(); i2++) {
                System.out.printf("  - %s | ID: %d, Статус: %s |\n",
                    milestones.getJSONObject(i2).getString("name"),
                    milestones.getJSONObject(i2).getInt("id"),
                    milestones.getJSONObject(i2).getString("status")
                );
                JSONArray tasks = milestones.getJSONObject(i2).getJSONArray("tasks");
                for(int i3 = 0; i3 < tasks.length(); i3++) {
                    System.out.printf("    - %s | ID: %d, Статус: %s |\n",
                        tasks.getJSONObject(i3).getString("name"),
                        tasks.getJSONObject(i3).getInt("id"),
                        tasks.getJSONObject(i3).getString("status")
                    );
                }
            }
        }
    }


    /*
     * Променя моментното съдържание на JSONObject jsonMainObj според
     * изискванията за задачата в точка II
     */
    public void changeStatus() {
        System.out.printf("\nМеню за промяна на статуса на задача:\n");

        ChainJSON chainJSON = null;
        while( chainJSON == null ) {
            System.out.printf("Въведете ID-то на задачата или 0 за изход: ");
            try {
                Scanner in = new Scanner(System.in);
                int taskID = in.nextInt();
                if( taskID == 0 ) return;
                chainJSON = getJSONObjectFromTaskID(taskID);
                if( chainJSON == null ) {
                    System.out.printf("Въведoхте невалидно ID на задача. Опитайте пак: \n");
                }
            }
            catch (InputMismatchException e) {
                System.out.printf("Въведеното от Вас ID не беше цяло положително число! Опитайте отново.\n");
            }
        }

        Optional<Status> newStatus = Optional.empty();
        while( newStatus.equals(Optional.empty()) ) {
            System.out.printf("Въведете новия статут на задачата ('Нов'|'В процес'|'Завършен'): ");
            Scanner in = new Scanner(System.in);
            String  st = in.nextLine();
            newStatus = Status.get(st);
            Status s = (Status) newStatus.orElse(null);
            if( s != null ) {
                // Веднага след промените на jsonMainObj се прави и обновяване на статусите в
                // майлстоун и проекта, към които е принадлежала задачата; след което и се записват във файла
                chainJSON.getTask().put("status", s.toString());
                chainJSON.updateMilestoneStatus();
                chainJSON.updateProjectStatus();
                this.writeToJSON();
            }
            else {
                System.out.printf("Въведения от Вас статут [%s] е невалиден! Опитайте отново.\n", st);
            }
        }
    }


    /*
     * При зададено ID ключ "task" създава и връща първия възможен class ChainJSON,
     * чийто "task" има зададеното ID. Ако не е намерена такава задача връща null.
     */
    private ChainJSON getJSONObjectFromTaskID(int askID) {
        ChainJSON chainJSON = new ChainJSON();

        JSONArray projects = this.jsonMainObj.getJSONArray("projects");
        for(int i1 = 0; i1 < projects.length(); i1++) {
            chainJSON.setProject(projects.getJSONObject(i1));
            JSONArray milestones = projects.getJSONObject(i1).getJSONArray("milestones");
            for(int i2 = 0; i2 < milestones.length(); i2++) {
                chainJSON.setMilestone(milestones.getJSONObject(i2));
                JSONArray tasks = milestones.getJSONObject(i2).getJSONArray("tasks");
                for(int i3 = 0; i3 < tasks.length(); i3++) {
                    chainJSON.setTask(tasks.getJSONObject(i3));
                    if( tasks.getJSONObject(i3).getInt("id") == askID ) {
                        return chainJSON;
                    }
                }
            }
        }

        return null;
    }


    /*
     * Този метод записва моментното състояние на JSONObject jsonMainObj във файла,
     * от който го е класа го е прочел при инициализацията.
     */
    private void writeToJSON() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.filename))) {
            this.jsonMainObj.write(writer, 2, 0);
            writer.write("\n");
        } catch (Exception ex) {
            System.err.println("Проблем при записването на промените.\n" + ex.getMessage());
        }
    }
}
