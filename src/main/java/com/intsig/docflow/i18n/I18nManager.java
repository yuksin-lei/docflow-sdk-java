package com.intsig.docflow.i18n;

import com.intsig.docflow.config.DocflowConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 国际化管理器
 *
 * <p>支持多语言消息翻译，单例模式</p>
 *
 * @author DocFlow SDK
 * @version 1.0.0
 */
public class I18nManager {

    private static volatile I18nManager instance;

    /**
     * 当前语言
     */
    private String currentLanguage = DocflowConstants.DEFAULT_LANGUAGE;

    /**
     * 翻译缓存：语言 -> 消息键 -> 消息内容
     */
    private final Map<String, Properties> translations = new HashMap<>();

    /**
     * 私有构造函数
     */
    private I18nManager() {
        loadTranslations();
    }

    /**
     * 获取单例实例
     *
     * @return I18nManager 实例
     */
    public static I18nManager getInstance() {
        if (instance == null) {
            synchronized (I18nManager.class) {
                if (instance == null) {
                    instance = new I18nManager();
                }
            }
        }
        return instance;
    }

    /**
     * 加载所有语言翻译
     */
    private void loadTranslations() {
        for (String lang : DocflowConstants.SUPPORTED_LANGUAGES) {
            loadLanguage(lang);
        }
    }

    /**
     * 加载指定语言的翻译
     *
     * @param language 语言代码
     */
    private void loadLanguage(String language) {
        String resourcePath = "/i18n/" + language + ".properties";
        Properties props = new Properties();

        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is != null) {
                // 使用 UTF-8 编码读取
                props.load(new InputStreamReader(is, StandardCharsets.UTF_8));
                translations.put(language, props);
            }
        } catch (IOException e) {
            // 加载失败时使用空的 Properties
            translations.put(language, new Properties());
        }
    }

    /**
     * 设置当前语言
     *
     * @param language 语言代码
     * @throws IllegalArgumentException 当语言不支持时
     */
    public void setLanguage(String language) {
        if (!translations.containsKey(language)) {
            throw new IllegalArgumentException(
                    "不支持的语言: " + language + ". 支持的语言: " + translations.keySet()
            );
        }
        this.currentLanguage = language;
    }

    /**
     * 获取当前语言
     *
     * @return 当前语言代码
     */
    public String getLanguage() {
        return currentLanguage;
    }

    /**
     * 获取所有可用语言
     *
     * @return 可用语言列表
     */
    public String[] getAvailableLanguages() {
        return translations.keySet().toArray(new String[0]);
    }

    /**
     * 翻译消息
     *
     * @param key  消息键
     * @param args 格式化参数
     * @return 翻译后的消息
     */
    public String translate(String key, Object... args) {
        Properties props = translations.get(currentLanguage);
        if (props == null) {
            return key;
        }

        String message = props.getProperty(key);
        if (message == null) {
            // 如果当前语言没有翻译，尝试使用默认语言
            if (!currentLanguage.equals(DocflowConstants.DEFAULT_LANGUAGE)) {
                props = translations.get(DocflowConstants.DEFAULT_LANGUAGE);
                if (props != null) {
                    message = props.getProperty(key);
                }
            }

            // 如果仍然没有翻译，返回键本身
            if (message == null) {
                return key;
            }
        }

        // 格式化消息
        if (args.length > 0) {
            try {
                return MessageFormat.format(message, args);
            } catch (IllegalArgumentException e) {
                // 格式化失败时返回原始消息
                return message;
            }
        }

        return message;
    }

    /**
     * 翻译消息（简写）
     *
     * @param key  消息键
     * @param args 格式化参数
     * @return 翻译后的消息
     */
    public String t(String key, Object... args) {
        return translate(key, args);
    }
}
