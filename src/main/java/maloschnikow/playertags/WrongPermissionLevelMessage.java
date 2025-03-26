package maloschnikow.playertags;

import com.mojang.brigadier.Message;

public class WrongPermissionLevelMessage implements Message {
    int requiredPermissionLevel;
    String toDoWhat;

    public WrongPermissionLevelMessage(int requiredPermissionLevel) {
        this.requiredPermissionLevel = requiredPermissionLevel;
    }
    public WrongPermissionLevelMessage(int requiredPermissionLevel, String toDoWhat) {
        this.requiredPermissionLevel = requiredPermissionLevel;
        this.toDoWhat = toDoWhat;
    }

    @Override
    public String getString() {

        if(toDoWhat.isEmpty()) {
            return "You need a permission level of at least " + Integer.toString(requiredPermissionLevel) + ".";
        }
        return "You need a permission level of at least " + Integer.toString(requiredPermissionLevel) + " to " + toDoWhat + ".";
    }
    
}