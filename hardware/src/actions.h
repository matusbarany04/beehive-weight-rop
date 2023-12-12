#pragma once
#include "enums.h"
#include "memory.h"

struct Action {
    ActionType type;
    String (*function)(JsonObject);
};

class ActionManager {

    public:

        ActionManager() {
            executedActions = new DynamicJsonDocument(JSON_OBJECT_SIZE(2));
        }

        void addAction(ActionType type, String (*function)(JsonObject)) {
            Action* action = new Action();
            action->type = type;
            action->function = function;

            if(actions == nullptr) actions = (Action**) malloc(sizeof(Action*));
            else actions = (Action**) realloc(actions, (actionCount + 1) * sizeof(Action*));

            actions[actionCount] = action;
            actionCount++;
        }

        void exec(ActionType type, long id, JsonObject params = JsonObject()) {
            Action* action = getAction(type);
            
            if(action != NULL) {
                params["actionID"] = id;
                String status = action->function(params);
                if(id != 0) {
                    DynamicJsonDocument prevDoc = *executedActions;
                    executedActions = new DynamicJsonDocument(executedActions->capacity() + JSON_OBJECT_SIZE(3));
                    executedActions->set(prevDoc);
                    JsonObject json = executedActions->createNestedObject();
                    json["id"] = id;
                    json["status"] = status;
                }
            }
        }

        String execGetStatus(ActionType type, long id, JsonObject params = JsonObject()) {
            Action* action = getAction(type);
            params["actionID"] = id;
            if(action != NULL) return action->function(params);
            else return "UNKNOWN_ERROR";
        }

        void schedule(ActionType type, long id, long executionTime, JsonObject params = JsonObject()) {
            ScheduledAction action = {id, type, "", executionTime};
            serializeJson(params, action.params);
            bool success = saveAction(action);

            DynamicJsonDocument prevDoc = *executedActions;
            executedActions = new DynamicJsonDocument(executedActions->capacity() + JSON_OBJECT_SIZE(3));
            executedActions->set(prevDoc);
            JsonObject json = executedActions->createNestedObject();
            json["id"] = id;
            json["status"] = success ? "SCHEDULED" : "ESP_NOT_ENOUGH_MEMORY";
        }

        String getExecutedActions() {
            String output;
            serializeJson(*executedActions, output);
            Serial.println(output);
            return output;
        }

    private:
        
        int actionCount;
        Action** actions;
        DynamicJsonDocument* executedActions;

        Action* getAction(ActionType type) {
            for(int i = 0; i < actionCount; i++) {
                Serial.println(actions[i]->type);
                if(actions[i]->type == type) return actions[i];
            }

            return NULL;
        }
};