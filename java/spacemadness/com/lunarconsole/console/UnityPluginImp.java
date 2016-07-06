package spacemadness.com.lunarconsole.console;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.unity3d.player.UnityPlayer;
import java.lang.ref.WeakReference;
import spacemadness.com.lunarconsole.core.LunarConsoleException;
import spacemadness.com.lunarconsole.utils.UIUtils;

public class UnityPluginImp implements ConsolePluginImp {
    private final WeakReference<UnityPlayer> playerRef;

    public UnityPluginImp(Activity activity) {
        UnityPlayer player = resolveUnityPlayerInstance(activity);
        if (player == null) {
            throw new LunarConsoleException("Can't initialize plugin: UnityPlayer instance not resolved");
        }
        this.playerRef = new WeakReference(player);
    }

    private static UnityPlayer resolveUnityPlayerInstance(Activity activity) {
        return resolveUnityPlayerInstance(UIUtils.getRootViewGroup(activity));
    }

    private static UnityPlayer resolveUnityPlayerInstance(ViewGroup root) {
        if (root instanceof UnityPlayer) {
            return (UnityPlayer) root;
        }
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof UnityPlayer) {
                return (UnityPlayer) child;
            }
            if (child instanceof ViewGroup) {
                ViewGroup player = resolveUnityPlayerInstance((ViewGroup) child);
                if (player != null) {
                    return player;
                }
            }
        }
        return null;
    }

    public View getTouchRecepientView() {
        return getPlayer();
    }

    private UnityPlayer getPlayer() {
        return (UnityPlayer) this.playerRef.get();
    }
}
