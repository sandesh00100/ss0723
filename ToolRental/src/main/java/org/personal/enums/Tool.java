package org.personal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

// TODO: Add better documentation
@AllArgsConstructor
@Getter
public enum Tool {
    CHNS("CHNS", ToolType.CHAINSAW, "Stihl"),
    LADW("LADW", ToolType.LADDER, "Werner"),
    JAKD("JAKD", ToolType.JACKHAMMER, "DeWalt"),
    JAKR("JAKR", ToolType.JACKHAMMER, "Ridgid");

    private static final Map<String, Tool> CODE_TO_TOOL = new HashMap<>();

    static {
        for (Tool tool: values()) {
            CODE_TO_TOOL.put(tool.code, tool);
        }
    }
    private final String code;
    private final ToolType type;
    private final String brand;

    /**
     * We could have probably gone without the name but just wanted to add it incase the name and the enum differ
     * @param code
     * @return
     */
    public static Tool getToolByCode(String code) {
        return CODE_TO_TOOL.get(code);
    }
}
