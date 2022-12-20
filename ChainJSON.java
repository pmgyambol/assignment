import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Scanner;
import java.util.Optional;

/*
 * Този клас пази JSONObject:
 *  за ключ "task"
 *  за ключ "milestone", под който се намира запасения по-горе ключ "task"
 *  за ключ "project",   под който се намира запасения по-горе ключ "milestone"
 */

public class ChainJSON {
    private JSONObject project;
    private JSONObject milestone;
    private JSONObject task;

    public JSONObject getProject()   {return this.project;}
    public JSONObject getMilestone() {return this.milestone;}
    public JSONObject getTask()      {return this.task;}

    public void setProject  (JSONObject project)   {this.project   = project;}
    public void setMilestone(JSONObject milestone) {this.milestone = milestone;}
    public void setTask     (JSONObject task)      {this.task      = task;}

    /*
     * Обновява информацията за ключа "milestone" съобразно изискванията III
     * на задачата
     */
    public void updateMilestoneStatus() {
        boolean allAreNew     = true;
        boolean allAreFinish  = true;
        JSONArray tasks = this.milestone.getJSONArray("tasks");
        for(int i = 0; i < tasks.length(); i++) {
            Optional<Status> status = Status.get(tasks.getJSONObject(i).getString("status"));
            Status s = (Status) status.orElse(null);
            if( s == Status.NEW      || s == Status.PROGRESS ) { allAreFinish = false; }
            if( s == Status.FINISHED || s == Status.PROGRESS ) { allAreNew    = false; }
        }
        if     ( allAreNew )    { this.milestone.put("status", Status.NEW.toString()); }
        else if( allAreFinish ) { this.milestone.put("status", Status.FINISHED.toString()); }
        else                    { this.milestone.put("status", Status.PROGRESS.toString()); }
    }
}
