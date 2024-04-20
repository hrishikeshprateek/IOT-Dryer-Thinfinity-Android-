package thundersharp.thinkfinity.dryer.boot.serverStat;

public interface OnServerStatus {
    void onServerAlive(String time);
    void onServerUnReachable(Exception e);
}
