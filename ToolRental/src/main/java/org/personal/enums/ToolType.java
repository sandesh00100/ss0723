package org.personal.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


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
