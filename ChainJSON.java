import org.json.JSONObject;
import org.json.JSONArray;

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
}
