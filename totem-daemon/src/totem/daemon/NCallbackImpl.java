package totem.daemon;

import com.sun.jna.Pointer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NCallbackImpl implements NPLibrary.NCallback {
    private static final Logger LOGGER = Logger.getLogger(NCallbackImpl.class.getName());
    private static final String[] NAMES = {
            "Status value changed",
            "Expansion info",
            "Queue emptied",
            "NSCanPrinters result",
            "NOpenPrinter result",
            "NResetPrinter result",
            "NFirmwareDL result",
            "NTCPPortLock result",
            "NBufferClearResult",
            "NBlockSendSetting result",
    };

    @Override
    public void invoke(Pointer name, int callbackType, int result1, int result2) {
        if (callbackType == 1) {
            String statusString = NPClient.statusString(result2);
            LOGGER.log(Level.INFO, name.getString(0) + " - " + NAMES[callbackType - 1]
                    + ": " + result1 + ", " + result2 + " - " + statusString);
        } else {
            LOGGER.log(Level.INFO, name.getString(0) + " - " + NAMES[callbackType - 1]
                    + ": " + result1 + ", " + result2);
        }
    }
}

