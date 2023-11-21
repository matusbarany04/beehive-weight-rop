#pragma once

struct Action {
    String name;
    void (*function)(float, unsigned int);
};

class ActionManager {

    public:

        void addAction(String name, void (*function)(float, unsigned int)) {
            Action action;
            action.name = name;
            action.function = function;

            if(actions == nullptr) actions = (Action*) malloc(sizeof(Action));
            else actions = (Action*) realloc(actions, sizeof(actions) + sizeof(Action));

            actions[sizeof(actions) / sizeof(Action) - 1] = action;
        }

        void exec(String actionName) {
            Action action = getAction(actionName);
            action.function();
        }

    private:
        
        Action* actions;

        Action getAction(String name) {
            for(int i = 0; i < sizeof(actions) / sizeof(Action); i += sizeof(Action)) {
                if(actions[i].name.equals(name)) return actions[i];
            }
        }
};