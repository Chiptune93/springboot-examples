package dev.chiptune.springboot.listener;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCacheEventListener implements CacheEventListener<Object, Object> {

    private static final Logger logger = LoggerFactory.getLogger(CustomCacheEventListener.class);

    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        logger.info("━━━━━━━━━━━━━━━━━BaseCacheEventListener START━☂☄");
        logger.info("☆ﾟ.*･｡ﾟ Cache Key : " + cacheEvent.getKey() + " ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
        logger.info("☆ﾟ.*･｡ﾟ Cache Old Value : " + cacheEvent.getOldValue() + " ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
        logger.info("☆ﾟ.*･｡ﾟ Cache New Value : " + cacheEvent.getNewValue() + " ᝯ◂ ࠫ‘֊‘ ࠫ▾ಎ➹");
        logger.info("━━━━━━━━━━━━━━━━━BaseCacheEventListener END━☂☄");
    }
}

