package nadav.tasher.lightool.modularity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ModularJSON {
    public static class Module {
        ArrayList<Module> subModules = null;
        String name = null, value = null;
        String[] options = null;

        public static class OptionModule extends Module {
            OptionModule(String name, String[] options) {
                super.name = name;
                super.options = options;
            }
        }

        public static class ValueModule extends Module {
            ValueModule(String name, String value) {
                super.name = name;
                super.value = value;
            }
        }

        public static class MasterModule extends Module {
            MasterModule(JSONObject masterObject) {
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
                            super.subModules.add(new MasterModule(masterObject.getJSONObject(name)));
                        }
                    }
                } catch (JSONException ignored) {
                }
            }
        }
    }
}
