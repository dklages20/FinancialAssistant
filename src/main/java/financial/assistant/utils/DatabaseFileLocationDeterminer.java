package financial.assistant.utils;

import financial.assistant.enums.OperatingSystem;

public class DatabaseFileLocationDeterminer {

    public static String determineDatabaseFileLocation() {
        OperatingSystem operatingSystem = determineOperatingSystem();
        if(operatingSystem != OperatingSystem.UNSUPPORTED) {
            String homeDirectory = System.getProperty("user.home");
            if(operatingSystem == OperatingSystem.WINDOWS) {
                return homeDirectory + "\\AppData\\Roaming\\FinancialAssistant\\db\\";
            } else if(operatingSystem == OperatingSystem.MAC) {
                return homeDirectory + "/Library/Application Support/FinancialAssistant/db/";
            } else {
                return homeDirectory + "/.FinancialAssistant/db/";
            }
        }
        return "";
    }

    public static OperatingSystem determineOperatingSystem() {
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if(osName.contains("mac")) {
            return OperatingSystem.MAC;
        } else if(osName.contains("nix") || osName.contains("nux")) {
            return OperatingSystem.LINUX;
        } else {
            return OperatingSystem.UNSUPPORTED;
        }
    }

}
