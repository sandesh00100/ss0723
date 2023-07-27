package org.demo.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * Tool type enum is used for storing the tool type metadata
 */
@AllArgsConstructor
@Getter
public enum ToolType {
    LADDER("Ladder", BigDecimal.valueOf(1.99), true, true, false),
    CHAINSAW("Chainsaw", BigDecimal.valueOf(1.49), true, false, true),
    JACKHAMMER("Jackhammer", BigDecimal.valueOf(2.99), true, false, false);

   private final String name;
   private final BigDecimal dailyCharge;
   private final boolean weekDayCharge;
   private final boolean weekendCharge;
   private final boolean holidayCharge;


    // Use this map to quickly look up the tool based on the code
   private static final Map<String, ToolType> NAME_TO_TOOL_TYPE = new HashMap<>();

    static {
        // Building tool type map
        for (ToolType toolType: values()) {
            NAME_TO_TOOL_TYPE.put(toolType.name, toolType);
        }
    }

    /**
     * Used to get the tool type enum by it's name
     */
    public static ToolType getToolByName(String name) {
        return NAME_TO_TOOL_TYPE.get(name);
    }
}
