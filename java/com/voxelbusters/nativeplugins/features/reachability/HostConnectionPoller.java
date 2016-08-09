package com.voxelbusters.nativeplugins.features.reachability;

import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import com.voxelbusters.nativeplugins.utilities.Debug;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HostConnectionPoller {
    private long connectionTimeOutPeriod = 60;
    private int currentRetryCount;
    private String ip = "8.8.8.8";
    private int maxRetryCount = 3;
    private int port = 56;
    private Future socketFutureTask = null;
    private float timeGapBetweenPolls = Default.HTTP_REQUEST_BACKOFF_MULT;

    HostConnectionPoller() {
    }

    void Start() {
        if (this.socketFutureTask != null) {
            this.socketFutureTask.cancel(true);
        }
        this.socketFutureTask = Executors.newSingleThreadExecutor().submit(new Runnable() {
            public void run() {
                InetSocketAddress address = new InetSocketAddress(HostConnectionPoller.this.getIp(), HostConnectionPoller.this.getPort());
                while (true) {
                    Socket socket = new Socket();
                    try {
                        socket.connect(address, (int) (HostConnectionPoller.this.getConnectionTimeOutPeriod() * 1000));
                        HostConnectionPoller.this.ReportConnectionSuccess();
                        socket.close();
                    } catch (IOException e) {
                        HostConnectionPoller.this.ReportConnectionFailure();
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep((long) (HostConnectionPoller.this.timeGapBetweenPolls * 1000.0f));
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }

    private void ReportConnectionFailure() {
        this.currentRetryCount++;
        if (this.currentRetryCount > getMaxRetryCount()) {
            NetworkReachabilityHandler.sendSocketConnectionStatus(false);
            this.currentRetryCount = 0;
        }
    }

    private void ReportConnectionSuccess() {
        NetworkReachabilityHandler.sendSocketConnectionStatus(true);
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public float getTimeGapBetweenPolls() {
        return this.timeGapBetweenPolls;
    }

    public void setTimeGapBetweenPolls(float timeGapBetweenPolls) {
        this.timeGapBetweenPolls = timeGapBetweenPolls;
    }

    public long getConnectionTimeOutPeriod() {
        return this.connectionTimeOutPeriod;
    }

    public void setConnectionTimeOutPeriod(int connectionTimeOutPeriod) {
        if (connectionTimeOutPeriod != 0) {
            this.connectionTimeOutPeriod = (long) connectionTimeOutPeriod;
        } else {
            Debug.warning(CommonDefines.NETWORK_CONNECTIVITY_TAG, "time out value should not be zero. Considering default 60 secs for timeout");
        }
    }

    public int getMaxRetryCount() {
        return this.maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }
}
