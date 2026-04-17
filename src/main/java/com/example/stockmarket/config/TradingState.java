package com.example.stockmarket.config;

public class TradingState {

    private static boolean tradingOpen = false;

    public static boolean isTradingOpen() {
        return tradingOpen;
    }

    public static void setTradingOpen(boolean status) {
        tradingOpen = status;
    }
}
