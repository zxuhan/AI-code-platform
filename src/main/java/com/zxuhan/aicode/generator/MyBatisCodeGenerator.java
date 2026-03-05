package com.zxuhan.aicode.generator;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Map;

/**
 * MyBatis Flex code generator.
 */
public class MyBatisCodeGenerator {

    // Tables to generate
    private static final String[] TABLE_NAMES = {"chat_history"};

    public static void main(String[] args) {
        // Load datasource metadata
        Dict dict = YamlUtil.loadByPath("application.yml");
        Map<String, Object> dataSourceConfig = dict.getByPath("spring.datasource");
        String url = String.valueOf(dataSourceConfig.get("url"));
        String username = String.valueOf(dataSourceConfig.get("username"));
        String password = String.valueOf(dataSourceConfig.get("password"));
        // Configure the datasource
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // Build configuration
        GlobalConfig globalConfig = createGlobalConfig();

        // Create the generator from the datasource and config
        Generator generator = new Generator(dataSource, globalConfig);

        // Generate code
        generator.generate();
    }


    // See: https://mybatis-flex.com/zh/others/codegen.html
    public static GlobalConfig createGlobalConfig() {
        // Build configuration
        GlobalConfig globalConfig = new GlobalConfig();

        // Set the base package. It is recommended to generate to a temporary directory first,
        // then move the generated code into the project.
        globalConfig.getPackageConfig()
                .setBasePackage("com.zxuhan.aicode.genresult");

        // Configure the tables to generate (omit setGenerateTable to generate all tables)
        globalConfig.getStrategyConfig()
                .setGenerateTable(TABLE_NAMES)
                // Default logical-delete column
                .setLogicDeleteColumn("isDelete");

        // Generate entities with Lombok
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setJdkVersion(21);

        // Generate mappers
        globalConfig.enableMapper();
        globalConfig.enableMapperXml();

        // Generate services
        globalConfig.enableService();
        globalConfig.enableServiceImpl();

        // Generate controllers
        globalConfig.enableController();

        // Configure generated comments to avoid unnecessary diffs later
        globalConfig.getJavadocConfig()
                .setAuthor("")
                .setSince("");
        return globalConfig;
    }
}

