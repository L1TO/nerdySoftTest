package products;

import java.util.ArrayList;
import java.util.List;

public class VirtualProductCodeManager {
    private static VirtualProductCodeManager instance;
    private List<String> usedCodes;

    public VirtualProductCodeManager() {
        this.usedCodes = new ArrayList<>();
    }

    public static VirtualProductCodeManager getInstance() {
        if (instance == null) {
            synchronized (VirtualProductCodeManager.class) {
                if (instance == null) {
                    instance = new VirtualProductCodeManager();
                }
            }
        }
        return instance;
    }

    public void useCode(String code) {
        if (!isCodeUsed(code)) {
            usedCodes.add(code);
        }
    }

    public boolean isCodeUsed(String code) {
        return usedCodes.equals(code);
    }
}
