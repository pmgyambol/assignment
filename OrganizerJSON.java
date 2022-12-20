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
}
