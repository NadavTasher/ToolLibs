package nadav.tasher.lightool.modularity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ModularJSON {
    public static class Module {
        public ArrayList<Module> subModules = null;
        public String name = null, value = null;
        public String[] options = null;

        public static class OptionModule extends Module {
            public OptionModule(String name, String[] options) {
                super.name = name;
                super.options = options;
            }
        }

        public static class ValueModule extends Module {
            public ValueModule(String name, String value) {
                super.name = name;
                super.value = value;
            }
        }

        public static class MasterModule extends Module {
            public MasterModule(JSONObject masterObject) {
                super.subModules = new ArrayList<>();
                try {
                    Iterator<String> types = masterObject.keys();
                    while (types.hasNext()) {
                        String name = types.next();
                        if (masterObject.get(name) instanceof String) {
                            super.subModules.add(new ValueModule(name, masterObject.getString(name)));
                        } else if (masterObject.get(name) instanceof JSONArray) {
                            JSONArray convertFrom = masterObject.getJSONArray(name);
                            String[] converted = new String[convertFrom.length()];
                            for (int p = 0; p < convertFrom.length(); p++) {
                                converted[p] = convertFrom.getString(p);
                            }
                            super.subModules.add(new OptionModule(name, converted));
                        } else if (masterObject.get(name) instanceof JSONObject) {
                            MasterModule masterModule = new MasterModule(masterObject.getJSONObject(name));
                            masterModule.name = name;
                            super.subModules.add(masterModule);
                        }
                    }
                } catch (JSONException ignored) {
                }
            }
        }
    }
}
