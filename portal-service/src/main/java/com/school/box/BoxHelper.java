package com.school.box;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIException;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.BoxUser;
import com.box.sdk.EncryptionAlgorithm;
import com.box.sdk.IAccessTokenCache;
import com.box.sdk.InMemoryLRUAccessTokenCache;
import com.box.sdk.JWTEncryptionPreferences;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public final class BoxHelper {

//    private static Environment environment;
//
//    @Autowired
//    public void setEnvironment(Environment env){
//        environment = env;
//    }

    static final String ENTERPRISE_ID = ConfigHelper.properties().getProperty("boxEnterpriseId");
    static final String CLIENT_ID = ConfigHelper.properties().getProperty("boxClientId");
    static final String CLIENT_SECRET = ConfigHelper.properties().getProperty("boxClientSecret");
    static final String PRIVATE_KEY_FILE = ConfigHelper.properties().getProperty("boxPrivateKeyFile");
    static final String PRIVATE_KEY_PASSWORD = ConfigHelper.properties().getProperty("boxPrivateKeyPassword");
    static final String PUBLIC_KEY_ID = ConfigHelper.properties().getProperty("boxPublicKeyId");

    static final String BOX_USER_ID_KEY = "box_id";
    static final String BOX_USER_NAME = "name";

    static final int MAX_CACHE_ENTRIES = 100;

    private static IAccessTokenCache accessTokenCache;
    private static JWTEncryptionPreferences jwtEncryptionPreferences;

    static {
        String privateKey;
        try {
            privateKey = new String(Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE)));
        } catch (Exception ex) {
            throw new BoxAPIException("Unable to read private key file: " + PRIVATE_KEY_FILE);
        }

        jwtEncryptionPreferences = new JWTEncryptionPreferences();
        jwtEncryptionPreferences.setPublicKeyID(PUBLIC_KEY_ID);
        jwtEncryptionPreferences.setPrivateKey(privateKey);
        jwtEncryptionPreferences.setPrivateKeyPassword(PRIVATE_KEY_PASSWORD);
        jwtEncryptionPreferences.setEncryptionAlgorithm(EncryptionAlgorithm.RSA_SHA_256);

        accessTokenCache = new InMemoryLRUAccessTokenCache(MAX_CACHE_ENTRIES);
    }

    /**
     * Uses the statically configured parameters to create Developer Edition connection.
     *
     * @return A new Box Developer Edition API connection with enterprise token leveraging an access token cache.
     */
    public static BoxDeveloperEditionAPIConnection adminClient() {
        try {
            BoxDeveloperEditionAPIConnection adminClient = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(
                    ENTERPRISE_ID, CLIENT_ID, CLIENT_SECRET, jwtEncryptionPreferences, accessTokenCache);
            return adminClient;
        } catch (BoxAPIException apiException) {
            apiException.printStackTrace();
            throw apiException;
        }
    }

    /**
     * Get a BoxDeveloperEditionAPIConnection that can be used by an App user to access Box.
     *
     * @param userId The UserId of a valid Box App User
     * @return BoxDeveloperEditionAPIConnection new Box Developer Edition connection with App User token
     */
    public static BoxDeveloperEditionAPIConnection userClient(String userId) {
        if (userId == null) {
            return null;
        }
        try {
            BoxDeveloperEditionAPIConnection userClient = BoxDeveloperEditionAPIConnection.getAppUserConnection(
                    userId, CLIENT_ID, CLIENT_SECRET, jwtEncryptionPreferences, accessTokenCache);
            return userClient;
        } catch (BoxAPIException apiException) {
            apiException.printStackTrace();
            throw apiException;
        }
    }

    /**
     * Returns the BoxId for the current App user.  This is looked up by searching for the User Name in this enterprises' list of users
     *
     *
     * @return String the boxId the of the current App User
     */
    public static String boxIdFromRequest(String appUserName) {
        String boxId = null;
        BoxAPIConnection api = BoxHelper.adminClient();
        Iterable<BoxUser.Info> appUsers = BoxUser.getAllEnterpriseUsers(api);
        if (!Iterables.isEmpty(appUsers)) {
            for (BoxUser.Info user : appUsers) {
                if (user.getName().toUpperCase().equals(appUserName.toUpperCase())) {
                    boxId = user.getID();
                    return boxId;
                }
            }
        }
        return boxId;
    }
}
