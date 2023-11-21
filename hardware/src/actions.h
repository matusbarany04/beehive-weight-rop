#pragma once

struct Action {
    long id;
    String type;
    void (*function)(JsonObject);
};

class ActionManager {

    public:

        void addAction(String type, void (*function)(JsonObject)) {
            Action action;
            action.type = type;
            action.function = function;

            if(actions == nullptr) actions = (Action*) malloc(sizeof(Action));
            else actions = (Action*) realloc(actions, sizeof(actions) + sizeof(Action));

            actions[sizeof(actions) / sizeof(Action) - 1] = action;
        }

        void exec(String type, long id, JsonObject params = JsonObject()) {
            Action action = getAction(type);
            action.function(params);
            JsonObject json;
            json["id"] = action.id;
            executedActions.add(id);
        }

        String getExecutedActions() {
            String output;
            serializeJson(executedActions, output);
            return output;
        }

    private:
        
        Action* actions;
        JsonArray executedActions;

        Action getAction(String type) {
            for(int i = 0; i < sizeof(actions) / sizeof(Action); i += sizeof(Action)) {
                if(actions[i].type.equals(type)) return actions[i];
            }

            return Action();
        }
};