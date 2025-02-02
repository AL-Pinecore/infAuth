package cn.infinitumstudios.infauth.data;

public class PlayerData {
    private String uuid;
    private String password;
    private boolean isAdmin;

    public PlayerData(String uuid, String password, boolean isAdmin) {
        this.uuid = uuid;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
