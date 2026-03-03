package com.zxuhan.aicode.constant;

/**
 * Application constants.
 */
public interface AppConstant {

    /**
     * Priority value used for featured applications.
     */
    Integer GOOD_APP_PRIORITY = 99;

    /**
     * Default application priority.
     */
    Integer DEFAULT_APP_PRIORITY = 0;

    /**
     * Root directory for generated applications.
     */
    String CODE_OUTPUT_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * Root directory for deployed applications.
     */
    String CODE_DEPLOY_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_deploy";

    /**
     * Host used for deployed applications.
     */
    String CODE_DEPLOY_HOST = "http://localhost";
}