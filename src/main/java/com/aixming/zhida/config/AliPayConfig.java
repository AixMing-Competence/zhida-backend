package com.aixming.zhida.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author AixMing
 * @since 2025-06-04 19:56:53
 */
@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AliPayConfig {

    private String url;

    private String appId;

    private String privateKey;

    private String alipayPublicKey;
    
    private String notifyUrl;

    @PostConstruct
    public void init() {
        // 设置参数（全局只需设置一次）
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";
        config.appId = this.appId;
        config.merchantPrivateKey = privateKey;
        config.alipayPublicKey = alipayPublicKey;
        config.notifyUrl = notifyUrl;
        Factory.setOptions(config);
        System.out.println("=======支付宝SDK初始化成功=======");
    }

    @Bean
    public AlipayClient alipayClient() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(url);
        //设置应用ID
        alipayConfig.setAppId(appId);
        //设置应用私钥
        alipayConfig.setPrivateKey(privateKey);
        //设置请求格式，固定值json
        //设置字符集
        alipayConfig.setCharset("utf-8");
        //设置签名类型
        alipayConfig.setSignType("RSA2");
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
        return alipayClient;
    }

}
