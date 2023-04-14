package com.mysiteforme.admin.redis;

import com.mysiteforme.admin.service.manager.WeiDianManager;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author yangqx
 * @description
 * @date 2023/4/13 17:32
 */
@Repository("redisWeiDianDAO")
public class RedisWeiDianDAO {
    private static Logger logger = LoggerFactory.getLogger(RedisWeiDianDAO.class);

    private static String prefix = "weidian:";

    // session 在redis过期时间是24小时24 * 60 * 60
    private static int tokenExpireTime = 86400;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String getAccessToken() {
        String key = "token";
        String token = (String) doRead(key);
        if(StringUtils.isEmpty(token)){
            token = WeiDianManager.getAccessToken();
            if(StringUtils.isNotEmpty(token)){
                doCreate(key, token);
            }
        }
        return token;
    }


    protected void doCreate(String key, Object value) {
        logger.debug("创建微店redis:{}", key);
        key = wrapKey(key);
        redisTemplate.opsForValue().set(key, value, tokenExpireTime, TimeUnit.SECONDS);
    }

    protected Object doRead(String key) {
        logger.debug("获取微店redis:{}", key);
        // 先从缓存中获取session，如果没有再去数据库中获取
        key = wrapKey(key);
        return redisTemplate.opsForValue().get(key);
    }

    protected void doUpdate(String key, Object value) {
        logger.debug("获取微店redis:{}", key);
        key = wrapKey(key);
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, value, tokenExpireTime, TimeUnit.SECONDS);
        }
        redisTemplate.expire(key, tokenExpireTime, TimeUnit.SECONDS);
    }

    // 删除session
    protected void doDelete(String key) {
        logger.debug("删除微店redis:{}", key);
        key = wrapKey(key);
        redisTemplate.delete(key);
    }


    /**
     * 包装key
     * @param key
     * @return
     */
    private String wrapKey(String key) {
        return prefix + key;
    }


}
