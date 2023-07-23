package org.personal.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@Getter
public enum ToolType {
    LADDER("Ladder", 1.99, true, true, false),
    CHAINSAW("Chainsaw", 1.49, true, false, true),
    JACKHAMMER("Jackhammer", 2.49, true, false, false);
    private final String name;
   private final double dailyCharge;
   private final boolean weekDayCharge;
   private final boolean weekendCharge;
   private final boolean holidayCharge;

   private static final Map<String, ToolType> NAME_TO_TOOL_TYPE = new HashMap<>();

    static {
        for (ToolType toolType: values()) {
            NAME_TO_TOOL_TYPE.put(toolType.name, toolType);
        }
    }

    public static ToolType getToolByName(String name) {
        return NAME_TO_TOOL_TYPE.get(name);
    }
}
