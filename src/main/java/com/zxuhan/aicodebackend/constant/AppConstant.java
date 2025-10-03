package com.zxuhan.aicodebackend.constant;

/**
 * App constant
 */
public interface AppConstant {

    /**
     * good app priority
     */
    Integer GOOD_APP_PRIORITY = 99;

    /**
     * default app priority
     */
    Integer DEFAULT_APP_PRIORITY = 0;

    /**
     * app output directory
     */
    String CODE_OUTPUT_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * app deploy directory
     */
    String CODE_DEPLOY_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_deploy";

    /**
     * app deploy host
     */
    String CODE_DEPLOY_HOST = "http://localhost";
}