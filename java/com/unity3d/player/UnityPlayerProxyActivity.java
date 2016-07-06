package com.unity3d.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class UnityPlayerProxyActivity extends Activity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = new Intent(this, UnityPlayerActivity.class);
        intent.addFlags(65536);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent);
    }
}
