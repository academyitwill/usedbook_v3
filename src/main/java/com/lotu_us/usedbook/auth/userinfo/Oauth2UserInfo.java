package com.lotu_us.usedbook.auth.userinfo;

import java.util.Map;

public interface Oauth2UserInfo {
    Map<String, Object> getAttributes();
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
