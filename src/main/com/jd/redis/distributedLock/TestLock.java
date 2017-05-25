package com.jd.redis.distributedLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.Jedis;

/**
 * Created by hspcadmin on 2017/3/21.
 */
public class TestLock {
	
    public static void main(String str[]){
    	
    	ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 2; i++) {
        	executor.execute(new RedisLockService("t_"+i));
        }
    }
}


class RedisLockService implements Runnable{

	private String name;
	
	public RedisLockService(String name){
		this.name = name;
	}
	
	public void run() {
		
		Jedis jedis = new Jedis("127.0.0.1", 6379);
        //锁等待时间(等待获取锁)   锁超时时间(获取锁后的持有时间)
        RedisLock redisLock = new RedisLock(jedis,"distributeLock", 100000, 200000);
        
        try {
			redisLock.lock();
			System.out.println("线程 "+name+" 获取了锁，进行文件修改");
	        Thread.sleep(2000);
	        
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			
		}finally{
			redisLock.unlock();
		}
        
        
        
		
		
		
	}
	
}








