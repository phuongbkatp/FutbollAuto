package com.appian.manchesterunitednews.data.app;

import android.content.Context;

import com.appian.manchesterunitednews.BuildConfig;
import com.appian.manchesterunitednews.R;

import java.util.HashMap;

public class AppConfig {
    private static final String TEST_ADMOB_BANNER = "ca-app-pub-3940256099942544/2934735716";
    private static final String TEST_ADMOB_INTERSTITIAL = "ca-app-pub-3940256099942544/4411468910";

    private int appId;
    private int currentSeasonId;
    private int tab1Id;
    private int tab2Id;
    private int tab3Id;
    private int tab4Id;
    private int tab5Id;
    private int categoryEventId;
    private String policyUrl;
    private String shareContent;
    private String admobAppId;
    private String admobBanner1;
    private String admobInterstitial;
    private String fbAdsNative1;
    private String fbAdsNative2;
    private HashMap<String, String> tabMapTitle;

    private AppConfig() {
        tabMapTitle = new HashMap<>();
    }

    public int getTeamId(Context context) {
        return context.getResources().getInteger(R.integer.team_id);
    }

    public String getAppKey() {
        return BuildConfig.FLAVOR;
    }

    public int getAppId() {
        return appId;
    }

    public int getCurrentSeasonId() {
        return currentSeasonId;
    }

    public String getAdmobAppId() {
        return admobAppId;
    }

    public String getAdmobBanner1() {
        if (isDebug()) {
            return TEST_ADMOB_BANNER;
        }
        return admobBanner1;
    }

    public String getAdmobInterstitial() {
        if (isDebug()) {
            return TEST_ADMOB_INTERSTITIAL;
        }
        return admobInterstitial;
    }

    public String getFbAdsNative1() {
        return fbAdsNative1;
    }

    public String getFbAdsNative2() {
        return fbAdsNative2;
    }

    public int getTabId(int tab) {
        switch (tab) {
            case 1:
                return tab1Id;
            case 2:
                return tab2Id;
            case 3:
                return tab3Id;
            case 4:
                return tab4Id;
            case 5:
                return tab5Id;
        }
        return 0;
    }

    public String getTabTitle(Context context, int tab) {
        String key = "tab" + tab + "_title_";
        String lang = AppConfigManager.getInstance().getLanguage(context);
        if (Language.VIETNAMESE.equals(lang)) {
            key += "vi";
        } else {
            key += "en";
        }
        return tabMapTitle.get(key);
    }

    public int getCategoryEventId() {
        return categoryEventId;
    }

    public String getPolicyUrl() {
        return policyUrl;
    }

    public String getShareContent() {
        return shareContent;
    }

    public static class Builder {
        private int teamId;
        private int teamSofaId;
        private int appId;
        private int currentSeasonId;
        private int tab1Id;
        private int tab2Id;
        private int tab3Id;
        private int tab4Id;
        private int tab5Id;
        private int categoryEventId;
        private String policyUrl;
        private String shareContent;
        private HashMap<String, String> tabMapTitle;
        private String admobAppId;
        private String admobBanner1;
        private String admobInterstitial1;
        private String fbAdsNative1;
        private String fbAdsNative2;

        Builder() {
            tabMapTitle = new HashMap<>();
        }

        Builder setTeamId(int teamId) {
            this.teamId = teamId;
            return this;
        }

        Builder setTeamSofaId(int teamSofaId) {
            this.teamSofaId = teamSofaId;
            return this;
        }

        Builder setAppId(int appId) {
            this.appId = appId;
            return this;
        }

        Builder setTab1Id(int tab1Id) {
            this.tab1Id = tab1Id;
            return this;
        }

        Builder setCategoryEventId(int categoryEventId) {
            this.categoryEventId = categoryEventId;
            return this;
        }

        Builder setTab2Id(int tab2Id) {
            this.tab2Id = tab2Id;
            return this;
        }

        Builder setTab3Id(int tab3Id) {
            this.tab3Id = tab3Id;
            return this;
        }

        Builder setTab4Id(int tab4Id) {
            this.tab4Id = tab4Id;
            return this;
        }

        Builder setTab5Id(int tab5Id) {
            this.tab5Id = tab5Id;
            return this;
        }

        Builder setCurrentSeasonId(int currentSeasonId) {
            this.currentSeasonId = currentSeasonId;
            return this;
        }

        Builder setPolicyUrl(String policyUrl) {
            this.policyUrl = policyUrl;
            return this;
        }

        Builder setShareContent(String shareContent) {
            this.shareContent = shareContent;
            return this;
        }

        Builder putTitle(String key, String value) {
            this.tabMapTitle.put(key, value);
            return this;
        }

        public Builder setAdmobAppId(String admobAppId) {
            this.admobAppId = admobAppId;
            return this;
        }

        public Builder setAdmobBanner1(String admobBanner1) {
            this.admobBanner1 = admobBanner1;
            return this;
        }

        public Builder setAdmobInterstitial1(String admobInterstitial1) {
            this.admobInterstitial1 = admobInterstitial1;
            return this;
        }

        public Builder setFbAdsNative1(String fbAdsNative1) {
            this.fbAdsNative1 = fbAdsNative1;
            return this;
        }

        public Builder setFbAdsNative2(String fbAdsNative2) {
            this.fbAdsNative2 = fbAdsNative2;
            return this;
        }

        AppConfig build() {
            AppConfig data = new AppConfig();
            data.appId = appId;
            data.currentSeasonId = currentSeasonId;
            data.tab1Id = tab1Id;
            data.tab2Id = tab2Id;
            data.tab3Id = tab3Id;
            data.tab4Id = tab4Id;
            data.tab5Id = tab5Id;
            data.categoryEventId = categoryEventId;
            data.policyUrl = policyUrl;
            data.shareContent = shareContent;
            data.tabMapTitle.clear();
            data.tabMapTitle.putAll(tabMapTitle);
            data.admobAppId = admobAppId;
            data.admobBanner1 = admobBanner1;
            data.admobInterstitial = admobInterstitial1;
            data.fbAdsNative1 = fbAdsNative1;
            data.fbAdsNative2 = fbAdsNative2;
            return data;
        }
    }

    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
