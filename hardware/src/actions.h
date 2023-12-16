#pragma once
#include "enums.h"
#include "memory.h"

struct Action {
    ActionType type;
    String (*function)(JsonObject);
    void (*postExecute)(String);
};

class ActionManager {

    public:

        ActionManager() {
            executedActions = new DynamicJsonDocument(JSON_OBJECT_SIZE(2));
        }

        void addAction(ActionType type, String (*function)(JsonObject), void (*postExecute)(String) = nullptr) {
            Action* action = new Action();
            action->type = type;
            action->function = function;
            action->postExecute = postExecute;

            if(actions == nullptr) actions = (Action**) malloc(sizeof(Action*));
            else actions = (Action**) realloc(actions, (actionCount + 1) * sizeof(Action*));

            actions[actionCount] = action;
            actionCount++;
        }

        void exec(ActionType type, long id, JsonObject params = JsonObject()) {
            if(actionWasExecuted(id)) return; 
            historyPush(id);

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
                    json["type"] = type;
                }
            }
        }

        void execAndThen(ActionType type, long id, JsonObject params = JsonObject(), void (*afterExecute)(long, String) = nullptr) {
            if(actionWasExecuted(id)) return; 
            historyPush(id);

            Action* action = getAction(type);
            Serial.println(id);
            params["actionID"] = id;

            if(action != NULL) {
                String status = action->function(params);
                if(afterExecute != nullptr) afterExecute(id, status);
                if(action->postExecute != nullptr) action->postExecute(status);
    
            } else afterExecute(0, "ACTION_NOT_RECOGNIZED");
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

        void runPostExecMethods() {
            for (JsonObject executedAction : executedActions->as<JsonArray>()) {
                Action* action = getAction(executedAction["type"]);
                if(action->postExecute != nullptr) action->postExecute(executedAction["status"]);
            }
            executedActions->clear();
        }

        bool resultQueueIsEmpty() {
            return executedActions->isNull();
        }

        void historyPush(long actionId) {
            historySize++;
            if(history == NULL) history = (long*) malloc(sizeof(long));
            else history = (long*) realloc(history, historySize * sizeof(long));
            history[historySize - 1] = actionId;
        }
        
        long actionWasExecuted(long actionId) {
            for(int i = 0; i < historySize; i++) {
                if(history[i] == actionId) return true;
            }
            return false;
        }

    private:
        
        long* history;
        int historySize;
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