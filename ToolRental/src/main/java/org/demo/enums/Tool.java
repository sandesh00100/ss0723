package org.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Tool is used to store all the tool constants.
 * If this wasn't a demo app a database would probably be more appropriate.
 */
@AllArgsConstructor
@Getter
public enum Tool {
    CHNS("CHNS", ToolType.CHAINSAW, "Stihl"),
    LADW("LADW", ToolType.LADDER, "Werner"),
    JAKD("JAKD", ToolType.JACKHAMMER, "DeWalt"),
    JAKR("JAKR", ToolType.JACKHAMMER, "Ridgid");

    // Use this map to quickly look up the tool based on the code
    private static final Map<String, Tool> CODE_TO_TOOL = new HashMap<>();

    static {
        // Set up code to tool map
        for (Tool tool: values()) {
            CODE_TO_TOOL.put(tool.code, tool);
        }
    }
    private final String code;
    private final ToolType type;
    private final String brand;

    /**
     * Used to get the Tool enum by its cod3
     */
    public static Tool getToolByCode(String code) {
        return CODE_TO_TOOL.get(code);
    }
}
