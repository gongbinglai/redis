package com.jd.redis.base;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisUtil {
    //Redis服务器IP
    private static String ADDR = "192.168.0.100";
    
    //Redis的端口号
    private static int PORT = 6379;
    
    //访问密码
    private static String AUTH = "admin";
    
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
    
    private static int TIMEOUT = 10000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
    private static ShardedJedisPool pool;
    
    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWait(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
    
    
    /**
     * 创建分片对象
     * @return
     */
        public static ShardedJedis createShardJedis() {
            
            //建立服务器列表
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
            
            //添加第一台服务器信息
            JedisShardInfo si = new JedisShardInfo("localhost", 6379);
            si.setPassword("123");
            shards.add(si);
            
            //添加第二台服务器信息
            si = new JedisShardInfo("localhost", 6399);
            si.setPassword("123");
            shards.add(si);
            //建立分片连接对象
            ShardedJedis jedis = new ShardedJedis(shards);        
            
            //建立分片连接对象,并指定Hash算法
            //ShardedJedis jedis = new ShardedJedis(shards,selfHash);
            return jedis;
        }
    
    
    
    
    
        private static void createPool() {
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
            JedisShardInfo si = new JedisShardInfo("localhost", 6379);
            si.setPassword("123");
            shards.add(si);
            si = new JedisShardInfo("localhost", 6399);
            si.setPassword("123");
            shards.add(si);
            pool = new ShardedJedisPool(new JedisPoolConfig(), shards);
        }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
