package net.kyrptonaught.lemclienthelper.SmallInv;

public interface SmallInvPlayerInv {
    void setIsSmall(boolean small);

    boolean getIsSmall();

    default boolean isSmallSupported() {
        return false;
    }
}