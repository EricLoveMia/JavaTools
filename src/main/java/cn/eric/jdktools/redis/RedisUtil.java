package cn.eric.jdktools.redis;

import cn.eric.jdktools.common.Config;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.SafeEncoder;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Redis工具类
 * Created by caicf on 2017/1/1.
 */
public class RedisUtil {

	private static final Logger logger = Logger.getLogger(RedisUtil.class);

	private static final Long RELEASE_SUCCESS = 1L;

	//默认缓存时间
	private static final int EXPIRE = 60000*5;
	private static RedisUtil instance;
	private static JedisPool jedisPool;
	private static ReentrantLock lock = new ReentrantLock();
	private RedisUtil() {
	}

	public static RedisUtil getInstance() {
		if (instance == null) {
			lock.lock();
			if (instance == null) {
				instance = new RedisUtil();
			}
			lock.unlock();
		}
		return instance;
	}


	/**
	 * 初始化JedisPool
	 */
	private void initJedisPool() {
		String maxTotal = Config.getConfInfo("redis.pool.maxTotal");
//		String maxActive = Config.getConfInfo("redis.pool.maxActive");
		String maxWait = Config.getConfInfo("redis.pool.maxWait");
		String minIdle = Config.getConfInfo("redis.pool.minIdle");
		String maxIdle = Config.getConfInfo("redis.pool.maxIdle");
		String testOnBorrow = Config.getConfInfo("redis.pool.testOnBorrow");
		String testOnReturn = Config.getConfInfo("redis.pool.testOnReturn");
		String host = Config.getConfInfo("redis.host");
		String port = Config.getConfInfo("redis.port");


		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(Integer.valueOf(maxTotal==null?"200":maxTotal));
		config.setMaxIdle(Integer.valueOf(maxIdle==null?"20":maxIdle));
		config.setMinIdle(Integer.valueOf(minIdle==null?"5":minIdle));
		config.setBlockWhenExhausted(true);
		config.setMaxWaitMillis(Long.valueOf(maxWait));


		config.setTestOnBorrow(Boolean.getBoolean(testOnBorrow==null?"true":testOnBorrow));
		config.setTestOnReturn(Boolean.getBoolean(testOnReturn==null?"true":testOnReturn));
		jedisPool = new JedisPool(
				config,
				host==null?"127.0.0.1":host,
				Integer.valueOf(port==null?"6379":port),
				Integer.parseInt(Config.getConfInfo("redis.timeout")),
				Config.getConfInfo("redis.password")
		);
	}

	/**
	 * 通用方法：从JedisPool中获取Jedis
	 *
	 * @return
	 */
	private Jedis getJedis() {
		Jedis jedis = null;
		if (jedisPool == null) {
			lock.lock();    //防止吃初始化时多线程竞争问题
			if(jedisPool != null){
				lock.unlock();
				return jedisPool.getResource();
			}
			initJedisPool();
			lock.unlock();
			logger.info("JedisPool init success！");
		}

		jedis = jedisPool.getResource();
		//String pass = Config.getConfInfo("redis.password");
		//jedis.auth(pass==null?"123abc":pass);
		return jedis;
	}

	/**
	 * 通用方法：释放Jedis
	 *
	 * @param jedis
	 */
	private void closeJedis(Jedis jedis) {
		jedis.close();
	}

//===========================================================
	/**
	 * 对Keys,以及存储结构为String、List、Set、HashMap类型的操作
	 */
	private final Keys keys = new Keys();
	private final Strings strings = new Strings();
	private final Lists lists = new Lists();
	private final Sets sets = new Sets();
	private final Hash hash = new Hash();
	private final SortSet sortset = new SortSet();
	private final Bitmap bitMap = new Bitmap();

	public Keys keys() {
		return keys;
	}

	public Strings strings() {
		return strings;
	}

	public Lists lists() {
		return lists;
	}

	public Sets sets() {
		return sets;
	}

	public Hash hash() {
		return hash;
	}

	public Bitmap bitmap() {return bitMap; }

	public SortSet sortSet() {
		return sortset;
	}
	//===========================================================

	//*******************************************Keys*******************************************//
	public class Keys {

		/**
		 * 设置过期时间
		 *
		 * @param key
		 * @param seconds
		 * @return 返回影响的记录数
		 */
		public long expire(String key, int seconds) {
			if (seconds <= 0) {
				return -1L;
			}
			Jedis jedis = getJedis();
			long result = jedis.expire(key, seconds);
			closeJedis(jedis);
			return result;
		}

		/**
		 * 设置过期时间，默认值为60000seconds
		 *
		 * @param key
		 */
		public long expire(String key) {
			return expire(key, EXPIRE);
		}

		/**
		 * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
		 *
		 * @param key
		 * @param timestamp 秒
		 * @return 影响的记录数
		 */
		public long expireAt(String key, long timestamp) {
			Jedis jedis = getJedis();
			long count = jedis.expireAt(key, timestamp);
			closeJedis(jedis);
			return count;
		}

		/**
		 * 查询key的过期时间
		 *
		 * @param key
		 * @return 以秒为单位的时间表示
		 */
		public long ttl(String key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			long len = sjedis.ttl(key);
			closeJedis(sjedis);
			return len;
		}

		/**
		 * 取消对key过期时间的设置
		 *
		 * @param key
		 * @return 影响的记录数
		 */
		public long persist(String key) {
			Jedis jedis = getJedis();
			long count = jedis.persist(key);
			closeJedis(jedis);
			return count;
		}

		/**
		 * 清空所有key
		 *
		 * @return
		 */
		public String flushAll() {
			Jedis jedis = getJedis();
			String stata = jedis.flushAll();
			closeJedis(jedis);
			return stata;
		}

		/**
		 * 判断key是否存在
		 *
		 * @param key
		 * @return boolean
		 */
		public boolean exists(String key) {
			Jedis sjedis = getJedis();
			boolean exis = sjedis.exists(key);
			closeJedis(sjedis);
			return exis;
		}

		/**
		 * 更改key
		 */
		public String rename(String oldKey, String newKey) {
			return rename(SafeEncoder.encode(oldKey),
					SafeEncoder.encode(newKey));
		}

		/**
		 * 更改key,仅当新key不存在时才执行
		 *
		 * @param oldKey
		 * @param newKey
		 * @return 状态码
		 */
		public long renamenx(String oldKey, String newKey) {
			Jedis jedis = getJedis();
			long status = jedis.renamenx(oldKey, newKey);
			closeJedis(jedis);
			return status;
		}

		/**
		 * 更改key
		 */
		public String rename(byte[] oldKey, byte[] newKey) {
			Jedis jedis = getJedis();
			String status = jedis.rename(oldKey, newKey);
			closeJedis(jedis);
			return status;
		}


		/**
		 * 删除keys对应的记录,可以是多个key
		 *
		 * @param keys
		 * @return 删除的记录数
		 */
		public long del(String... keys) {
			Jedis jedis = getJedis();
			long count = jedis.del(keys);
			closeJedis(jedis);
			return count;
		}

		/**
		 * 删除keys对应的记录,可以是多个key
		 *
		 * @param keys
		 * @return 删除的记录数
		 */
		public long del(byte[]... keys) {
			Jedis jedis = getJedis();
			long count = jedis.del(keys);
			closeJedis(jedis);
			return count;
		}


		/**
		 * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
		 *
		 * @param key
		 * @return List<String> 集合的全部记录
		 **/
		public List<String> sort(String key) {
			Jedis sjedis = getJedis();
			List<String> list = sjedis.sort(key);
			closeJedis(sjedis);
			return list;
		}

		/**
		 * 对List,Set,SortSet进行排序或limit
		 *
		 * @param key
		 * @param parame 定义排序类型或limit的起止位置.
		 * @return List<String> 全部或部分记录
		 **/
		public List<String> sort(String key, SortingParams parame) {
			Jedis jedis = getJedis();
			List<String> list = jedis.sort(key, parame);
			closeJedis(jedis);
			return list;
		}

		/**
		 * 返回指定key存储的类型
		 *
		 * @param key
		 * @return String string|list|set|zset|hash
		 **/
		public String type(String key) {
			Jedis sjedis = getJedis();
			String type = sjedis.type(key);
			closeJedis(sjedis);
			return type;
		}

		/**
		 * 查找所有匹配给定的模式的键
		 *
		 * @param pattern 的表达式,*表示多个，？表示一个
		 */
		public Set<String> keys(String pattern) {
			Jedis jedis = getJedis();
			Set<String> set = jedis.keys(pattern);
			closeJedis(jedis);
			return set;
		}
	}

	//*******************************************Sets*******************************************//
	public class Sets {

		/**
		 * 向Set添加一条记录，如果member已存在返回0,否则返回1
		 *
		 * @param key
		 * @param member
		 * @return 操作码, 0或1
		 */
		public long sadd(String key, String member) {
			Jedis jedis = getJedis();
			long s = jedis.sadd(key, member);
			closeJedis(jedis);
			return s;
		}

		public long sadd(byte[] key, byte[] member) {
			Jedis jedis = getJedis();
			long s = jedis.sadd(key, member);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 获取给定key中元素个数
		 *
		 * @param key
		 * @return 元素个数
		 */
		public long scard(String key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			long len = sjedis.scard(key);
			closeJedis(sjedis);
			return len;
		}

		/**
		 * 返回从第一组和所有的给定集合之间的差异的成员
		 *
		 * @param keys
		 * @return 差异的成员集合
		 */
		public Set<String> sdiff(String... keys) {
			Jedis jedis = getJedis();
			Set<String> set = jedis.sdiff(keys);
			closeJedis(jedis);
			return set;
		}

		/**
		 * 这个命令等于sdiff,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在，则覆盖。
		 *
		 * @param newKey 新结果集的key
		 * @param keys   比较的集合
		 * @return 新集合中的记录数
		 **/
		public long sdiffstore(String newKey, String... keys) {
			Jedis jedis = getJedis();
			long s = jedis.sdiffstore(newKey, keys);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 返回给定集合交集的成员,如果其中一个集合为不存在或为空，则返回空Set
		 *
		 * @param keys
		 * @return 交集成员的集合
		 **/
		public Set<String> sinter(String... keys) {
			Jedis jedis = getJedis();
			Set<String> set = jedis.sinter(keys);
			closeJedis(jedis);
			return set;
		}

		/**
		 * 这个命令等于sinter,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在，则覆盖。
		 *
		 * @param newKey 新结果集的key
		 * @param keys   比较的集合
		 * @return 新集合中的记录数
		 **/
		public long sinterstore(String newKey, String... keys) {
			Jedis jedis = getJedis();
			long s = jedis.sinterstore(newKey, keys);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 确定一个给定的值是否存在
		 *
		 * @param key
		 * @param member 要判断的值
		 * @return 存在返回1，不存在返回0
		 **/
		public boolean sismember(String key, String member) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			boolean s = sjedis.sismember(key, member);
			closeJedis(sjedis);
			return s;
		}

		/**
		 * 返回集合中的所有成员
		 *
		 * @param key
		 * @return 成员集合
		 */
		public Set<String> smembers(String key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			Set<String> set = sjedis.smembers(key);
			closeJedis(sjedis);
			return set;
		}

		public Set<byte[]> smembers(byte[] key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			Set<byte[]> set = sjedis.smembers(key);
			closeJedis(sjedis);
			return set;
		}

		/**
		 * 将成员从源集合移出放入目标集合 <br/>
		 * 如果源集合不存在或不包哈指定成员，不进行任何操作，返回0<br/>
		 * 否则该成员从源集合上删除，并添加到目标集合，如果目标集合中成员已存在，则只在源集合进行删除
		 *
		 * @param srckey 源集合
		 * @param dstkey 目标集合
		 * @param member 源集合中的成员
		 * @return 状态码，1成功，0失败
		 */
		public long smove(String srckey, String dstkey, String member) {
			Jedis jedis = getJedis();
			long s = jedis.smove(srckey, dstkey, member);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 从集合中删除成员
		 *
		 * @param key
		 * @return 被删除的成员
		 */
		public String spop(String key) {
			Jedis jedis = getJedis();
			String s = jedis.spop(key);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 从集合中删除指定成员
		 *
		 * @param key
		 * @param member 要删除的成员
		 * @return 状态码，成功返回1，成员不存在返回0
		 */
		public long srem(String key, String member) {
			Jedis jedis = getJedis();
			long s = jedis.srem(key, member);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 合并多个集合并返回合并后的结果，合并后的结果集合并不保存<br/>
		 *
		 * @param keys
		 * @return 合并后的结果集合
		 */
		public Set<String> sunion(String... keys) {
			Jedis jedis = getJedis();
			Set<String> set = jedis.sunion(keys);
			closeJedis(jedis);
			return set;
		}

		/**
		 * 合并多个集合并将合并后的结果集保存在指定的新集合中，如果新集合已经存在则覆盖
		 *
		 * @param newKey 新集合的key
		 * @param keys   要合并的集合
		 **/
		public long sunionstore(String newKey, String... keys) {
			Jedis jedis = getJedis();
			long s = jedis.sunionstore(newKey, keys);
			closeJedis(jedis);
			return s;
		}
	}

	//*******************************************SortSet*******************************************//
	public class SortSet {

		/**
		 * 向集合中增加一条记录,如果这个值已存在，这个值对应的权重将被置为新的权重
		 *
		 * @param key
		 * @param score  权重
		 * @param member 要加入的值，
		 * @return 状态码 1成功，0已存在member的值
		 */
		public long zadd(String key, double score, String member) {
			Jedis jedis = getJedis();
			long s = jedis.zadd(key, score, member);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 获取集合中元素的数量
		 *
		 * @param key
		 * @return 如果返回0则集合不存在
		 */
		public long zcard(String key) {
			Jedis sjedis = getJedis();
			long len = sjedis.zcard(key);
			closeJedis(sjedis);
			return len;
		}

		/**
		 * 获取指定权重区间内集合的数量
		 *
		 * @param key
		 * @param min 最小排序位置
		 * @param max 最大排序位置
		 */
		public long zcount(String key, double min, double max) {
			Jedis sjedis = getJedis();
			long len = sjedis.zcount(key, min, max);
			closeJedis(sjedis);
			return len;
		}

		/**
		 * 获得set的长度
		 *
		 * @param key
		 * @return
		 */
		public long zlength(String key) {
			long len = 0;
			Set<String> set = zrange(key, 0, -1);
			len = set.size();
			return len;
		}

		/**
		 * 权重增加给定值，如果给定的member已存在
		 *
		 * @param key
		 * @param score  要增的权重
		 * @param member 要插入的值
		 * @return 增后的权重
		 */
		public double zincrby(String key, double score, String member) {
			Jedis jedis = getJedis();
			double s = jedis.zincrby(key, score, member);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
		 *
		 * @param key
		 * @param start 开始位置(包含)
		 * @param end   结束位置(包含)
		 * @return Set<String>
		 */
		public Set<String> zrange(String key, int start, int end) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			Set<String> set = sjedis.zrange(key, start, end);
			closeJedis(sjedis);
			return set;
		}

		/**
		 * 返回指定权重区间的元素集合
		 *
		 * @param key
		 * @param min 上限权重
		 * @param max 下限权重
		 * @return Set<String>
		 */
		public Set<String> zrangeByScore(String key, double min, double max) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			Set<String> set = sjedis.zrangeByScore(key, min, max);
			closeJedis(sjedis);
			return set;
		}

		/**
		 * 获取指定值在集合中的位置，集合排序从低到高
		 *
		 * @param key
		 * @param member
		 * @return long 位置
		 */
		public long zrank(String key, String member) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			long index = sjedis.zrank(key, member);
			closeJedis(sjedis);
			return index;
		}

		/**
		 * 获取指定值在集合中的位置，集合排序从高到低
		 *
		 * @param key
		 * @param member
		 * @return long 位置
		 */
		public long zrevrank(String key, String member) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			long index = sjedis.zrevrank(key, member);
			closeJedis(sjedis);
			return index;
		}

		/**
		 * 从集合中删除成员
		 *
		 * @param key
		 * @param member
		 * @return 返回1成功
		 */
		public long zrem(String key, String member) {
			Jedis jedis = getJedis();
			long s = jedis.zrem(key, member);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 删除
		 *
		 * @param key
		 * @return
		 */
		public long zrem(String key) {
			Jedis jedis = getJedis();
			long s = jedis.del(key);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 删除给定位置区间的元素
		 *
		 * @param key
		 * @param start 开始区间，从0开始(包含)
		 * @param end   结束区间,-1为最后一个元素(包含)
		 * @return 删除的数量
		 */
		public long zremrangeByRank(String key, int start, int end) {
			Jedis jedis = getJedis();
			long s = jedis.zremrangeByRank(key, start, end);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 删除给定权重区间的元素
		 *
		 * @param key
		 * @param min 下限权重(包含)
		 * @param max 上限权重(包含)
		 * @return 删除的数量
		 */
		public long zremrangeByScore(String key, double min, double max) {
			Jedis jedis = getJedis();
			long s = jedis.zremrangeByScore(key, min, max);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 获取给定区间的元素，原始按照权重由高到低排序
		 *
		 * @param key
		 * @param start
		 * @param end
		 * @return Set<String>
		 */
		public Set<String> zrevrange(String key, int start, int end) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			Set<String> set = sjedis.zrevrange(key, start, end);
			closeJedis(sjedis);
			return set;
		}

		/**
		 * 获取给定值在集合中的权重
		 *
		 * @param key
		 * @param memebr
		 * @return double 权重
		 */
		public double zscore(String key, String memebr) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			Double score = sjedis.zscore(key, memebr);
			closeJedis(sjedis);
			if (score != null)
				return score;
			return 0;
		}
	}

	//*******************************************Hash*******************************************//
	public class Hash {

		/**
		 * 从hash中删除指定的存储
		 *
		 * @param key
		 * @param fieid 存储的名字
		 * @return 状态码，1成功，0失败
		 */
		public long hdel(String key, String fieid) {
			Jedis jedis = getJedis();
			long s = jedis.hdel(key, fieid);
			closeJedis(jedis);
			return s;
		}

		public long hdel(String key) {
			Jedis jedis = getJedis();
			long s = jedis.del(key);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 测试hash中指定的存储是否存在
		 *
		 * @param key
		 * @param fieid 存储的名字
		 * @return 1存在，0不存在
		 */
		public boolean hexists(String key, String fieid) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			boolean s = sjedis.hexists(key, fieid);
			closeJedis(sjedis);
			return s;
		}

		/**
		 * 返回hash中指定存储位置的值
		 *
		 * @param key
		 * @param fieid 存储的名字
		 * @return 存储对应的值
		 */
		public String hget(String key, String fieid) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			String s = sjedis.hget(key, fieid);
			closeJedis(sjedis);
			return s;
		}

		public byte[] hget(byte[] key, byte[] fieid) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			byte[] s = sjedis.hget(key, fieid);
			closeJedis(sjedis);
			return s;
		}

		/**
		 * 以Map的形式返回hash中的存储和值
		 *
		 * @param key
		 * @return Map<Strinig,String>
		 */
		public Map<String, String> hgetAll(String key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			Map<String, String> map = sjedis.hgetAll(key);
			closeJedis(sjedis);
			return map;
		}

		/**
		 * 添加一个对应关系
		 *
		 * @param key
		 * @param fieid
		 * @param value
		 * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
		 **/
		public long hset(String key, String fieid, String value) {
			Jedis jedis = getJedis();
			long s = jedis.hset(key, fieid, value);
			closeJedis(jedis);
			return s;
		}

		public long hset(String key, String fieid, byte[] value) {
			Jedis jedis = getJedis();
			long s = jedis.hset(key.getBytes(), fieid.getBytes(), value);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 添加对应关系，只有在fieid不存在时才执行
		 *
		 * @param key
		 * @param fieid
		 * @param value
		 * @return 状态码 1成功，0失败fieid已存
		 **/
		public long hsetnx(String key, String fieid, String value) {
			Jedis jedis = getJedis();
			long s = jedis.hsetnx(key, fieid, value);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 获取hash中value的集合
		 *
		 * @param key
		 * @return List<String>
		 */
		public List<String> hvals(String key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			List<String> list = sjedis.hvals(key);
			closeJedis(sjedis);
			return list;
		}

		/**
		 * 在指定的存储位置加上指定的数字，存储位置的值必须可转为数字类型
		 *
		 * @param key
		 * @param fieid 存储位置
		 * @param value 要增加的值,可以是负数
		 * @return 增加指定数字后，存储位置的值
		 */
		public long hincrby(String key, String fieid, long value) {
			Jedis jedis = getJedis();
			long s = jedis.hincrBy(key, fieid, value);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 返回指定hash中的所有存储名字,类似Map中的keySet方法
		 *
		 * @param key
		 * @return Set<String> 存储名称的集合
		 */
		public Set<String> hkeys(String key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			Set<String> set = sjedis.hkeys(key);
			closeJedis(sjedis);
			return set;
		}

		/**
		 * 获取hash中存储的个数，类似Map中size方法
		 *
		 * @param key
		 * @return long 存储的个数
		 */
		public long hlen(String key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			long len = sjedis.hlen(key);
			closeJedis(sjedis);
			return len;
		}

		/**
		 * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
		 *
		 * @param key
		 * @param fieids 存储位置
		 * @return List<String>
		 */
		public List<String> hmget(String key, String... fieids) {
			Jedis sjedis = getJedis();
			List<String> list = sjedis.hmget(key, fieids);
			closeJedis(sjedis);
			return list;
		}

		public List<byte[]> hmget(byte[] key, byte[]... fieids) {
			Jedis sjedis = getJedis();
			List<byte[]> list = sjedis.hmget(key, fieids);
			closeJedis(sjedis);
			return list;
		}

		/**
		 * 添加对应关系，如果对应关系已存在，则覆盖
		 *
		 * @param key
		 * @param map 对应关系
		 * @return 状态，成功返回OK
		 */
		public String hmset(String key, Map<String, String> map) {
			Jedis jedis = getJedis();
			String s = jedis.hmset(key, map);
			closeJedis(jedis);
			return s;
		}

		/**
		 * 添加对应关系，如果对应关系已存在，则覆盖
		 *
		 * @param key
		 * @param map 对应关系
		 * @return 状态，成功返回OK
		 */
		public String hmset(byte[] key, Map<byte[], byte[]> map) {
			Jedis jedis = getJedis();
			String s = jedis.hmset(key, map);
			closeJedis(jedis);
			return s;
		}

	}


	//*******************************************Strings*******************************************//
	public class Strings {
		/**
		 * 根据key获取记录
		 *
		 * @param key
		 * @return 值
		 */
		public String get(String key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			String value = sjedis.get(key);
			closeJedis(sjedis);
			return value;
		}

		/**
		 * 根据key获取记录
		 *
		 * @param key
		 * @return 值
		 */
		public byte[] get(byte[] key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			byte[] value = sjedis.get(key);
			closeJedis(sjedis);
			return value;
		}

		public long getLifeCount(String key){
			Jedis jedis = getJedis();
			closeJedis(jedis);
			return jedis.ttl(key);
		}

		/**
		 * 添加有过期时间的记录
		 *
		 * @param key
		 * @param seconds 过期时间，以秒为单位
		 * @param value
		 * @return String 操作状态
		 */
		public String setEx(String key, int seconds, String value) {
			Jedis jedis = getJedis();
			String str = jedis.setex(key, seconds, value);
			closeJedis(jedis);
			return str;
		}

		/**
		 * 添加有过期时间的记录
		 *
		 * @param key
		 * @param seconds 过期时间，以秒为单位
		 * @param value
		 * @return String 操作状态
		 */
		public String setEx(byte[] key, int seconds, byte[] value) {
			Jedis jedis = getJedis();
			String str = jedis.setex(key, seconds, value);
			closeJedis(jedis);
			return str;
		}

		/**
		 * 添加一条记录，仅当给定的key不存在时才插入
		 *
		 * @param key
		 * @param value
		 * @return long 状态码，1插入成功且key不存在，0未插入，key存在
		 */
		public long setnx(String key, String value) {
			Jedis jedis = getJedis();
			long str = jedis.setnx(key, value);
			closeJedis(jedis);
			return str;
		}

		/**
		 * 添加一条记录，仅当给定的key不存在时才插入
		 *
		 * @param key
		 * @param value
		 * @return String "OK" 成功
		 */
		public String setnx(String key, String value,long expireTime) {
			Jedis jedis = getJedis();
			String str = jedis.set(key,value,"NX","EX",expireTime);
			closeJedis(jedis);
			return str;
		}

		/**
		 * 添加记录,如果记录已存在将覆盖原有的value
		 *
		 * @param key
		 * @param value
		 * @return 状态码
		 */
		public String set(String key, String value) {
			return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
		}

		/**
		 * 添加记录,如果记录已存在将覆盖原有的value
		 *
		 * @param key
		 * @param value
		 * @return 状态码
		 */
		public String set(String key, byte[] value) {
			return set(SafeEncoder.encode(key), value);
		}

		/**
		 * 添加记录,如果记录已存在将覆盖原有的value
		 *
		 * @param key
		 * @param value
		 * @return 状态码
		 */
		public String set(byte[] key, byte[] value) {
			Jedis jedis = getJedis();
			String status = jedis.set(key, value);
			closeJedis(jedis);
			return status;
		}

		/**
		 * 从指定位置开始插入数据，插入的数据会覆盖指定位置以后的数据<br/>
		 * 例:String str1="123456789";<br/>
		 * 对str1操作后setRange(key,4,0000)，str1="123400009";
		 *
		 * @param key
		 * @param offset
		 * @param value
		 * @return long value的长度
		 */
		public long setRange(String key, long offset, String value) {
			Jedis jedis = getJedis();
			long len = jedis.setrange(key, offset, value);
			closeJedis(jedis);
			return len;
		}

		/**
		 * 在指定的key中追加value
		 *
		 * @param key
		 * @param value
		 * @return long 追加后value的长度
		 **/
		public long append(String key, String value) {
			Jedis jedis = getJedis();
			long len = jedis.append(key, value);
			closeJedis(jedis);
			return len;
		}

		/**
		 * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用
		 *
		 * @param key
		 * @param number 要减去的值
		 * @return long 减指定值后的值
		 */
		public long decrBy(String key, long number) {
			Jedis jedis = getJedis();
			long len = jedis.decrBy(key, number);
			closeJedis(jedis);
			return len;
		}

		/**
		 * <b>可以作为获取唯一id的方法</b><br/>
		 * 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
		 *
		 * @param key
		 * @param number 要减去的值
		 * @return long 相加后的值
		 */
		public long incrBy(String key, long number) {
			Jedis jedis = getJedis();
			long len = jedis.incrBy(key, number);
			closeJedis(jedis);
			return len;
		}

		/**
		 * 对指定key对应的value进行截取
		 *
		 * @param key
		 * @param startOffset 开始位置(包含)
		 * @param endOffset   结束位置(包含)
		 * @return String 截取的值
		 */
		public String getrange(String key, long startOffset, long endOffset) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			String value = sjedis.getrange(key, startOffset, endOffset);
			closeJedis(sjedis);
			return value;
		}

		/**
		 * 获取并设置指定key对应的value<br/>
		 * 如果key存在返回之前的value,否则返回null
		 *
		 * @param key
		 * @param value
		 * @return String 原始value或null
		 */
		public String getSet(String key, String value) {
			Jedis jedis = getJedis();
			String str = jedis.getSet(key, value);
			closeJedis(jedis);
			return str;
		}

		/**
		 * 批量获取记录,如果指定的key不存在返回List的对应位置将是null
		 *
		 * @param keys
		 * @return List<String> 值得集合
		 */
		public List<String> mget(String... keys) {
			Jedis jedis = getJedis();
			List<String> str = jedis.mget(keys);
			closeJedis(jedis);
			return str;
		}

		/**
		 * 批量存储记录
		 *
		 * @param keysvalues 例:keysvalues="key1","value1","key2","value2";
		 * @return String 状态码
		 */
		public String mset(String... keysvalues) {
			Jedis jedis = getJedis();
			String str = jedis.mset(keysvalues);
			closeJedis(jedis);
			return str;
		}

		/**
		 * 获取key对应的值的长度
		 *
		 * @param key
		 * @return value值得长度
		 */
		public long strlen(String key) {
			Jedis jedis = getJedis();
			long len = jedis.strlen(key);
			closeJedis(jedis);
			return len;
		}

		/**
		 * 加锁操作
		 * @author Eric
		 * @date 15:01 2019/2/13
		 * @params lockName
		 * @params acquireTimeout
		 * @params timeout
		 * @throws
		 * @return java.lang.String
		 **/
		public String lockWithTimeout(String lockName,long acquireTimeout,long timeout){
			// 获取链接

			Jedis jedis = getJedis();
			// 随机生成value
			String identifier = UUID.randomUUID().toString().replace("-","");
			// 锁名
			String lockKey = "lock:" + lockName;
			// 上锁超时时间，超时则自动释放锁
			int lockExpire = (int)timeout/1000;

			// 获取锁的超时时间，超过这个时间则放弃获取锁
			long end = System.currentTimeMillis() + acquireTimeout;
			try {
				while (System.currentTimeMillis() < end) {
					if ("OK".equals(jedis.set(lockKey, identifier, "NX", "EX", lockExpire))) {
						return identifier;
					}
					// 这只是一个
					if (jedis.ttl(lockKey) == -1) {
						jedis.expire(lockKey, lockExpire);
					}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}catch(JedisException e){
				e.printStackTrace();
			}finally {
				if(jedis != null){
					closeJedis(jedis);
				}
			}

			return identifier;
		}

		public boolean releaseLock(String lockName,String identifier){
			// 获取链接
			Jedis jedis = getJedis();
			String lockKey = "lock:" + lockName;
			boolean retFlag = false;

			try{
				while(true){
					// 监视lock，准备开始事务
					jedis.watch(lockKey);
					if(identifier.equals(jedis.get(lockKey))){
						Transaction transaction = jedis.multi();
						transaction.del(lockKey);
						List<Object> exec = transaction.exec();
						if(exec == null){
							continue;
						}
						retFlag = true;
					}
					jedis.unwatch();
					break;
				}
			}catch(JedisException e){
				e.printStackTrace();
			}finally {
				if(jedis != null){
					closeJedis(jedis);
				}
			}
			return retFlag;
		}

		public boolean releaseLockByScript(String lockName,String identifier){
			// 获取链接
			Jedis jedis = getJedis();
			String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

			try {
				Object result = jedis.eval(script, Collections.singletonList(lockName), Collections.singletonList(identifier));
				if (RELEASE_SUCCESS.equals(result)) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(jedis != null){
					closeJedis(jedis);
				}
			}
			return false;
		}
	}


	//*******************************************Lists*******************************************//
	public class Lists {
		/**
		 * List长度
		 *
		 * @param key
		 * @return 长度
		 */
		public long llen(String key) {
			return llen(SafeEncoder.encode(key));
		}

		/**
		 * List长度
		 *
		 * @param key
		 * @return 长度
		 */
		public long llen(byte[] key) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			long count = sjedis.llen(key);
			closeJedis(sjedis);
			return count;
		}

		/**
		 * 覆盖操作,将覆盖List中指定位置的值
		 *
		 * @param key
		 * @param index 位置
		 * @param value 值
		 * @return 状态码
		 */
		public String lset(byte[] key, int index, byte[] value) {
			Jedis jedis = getJedis();
			String status = jedis.lset(key, index, value);
			closeJedis(jedis);
			return status;
		}

		/**
		 * 覆盖操作,将覆盖List中指定位置的值
		 *
		 * @param
		 * @param index 位置
		 * @param value 值
		 * @return 状态码
		 */
		public String lset(String key, int index, String value) {
			return lset(SafeEncoder.encode(key), index,
					SafeEncoder.encode(value));
		}

		/**
		 * 获取List中指定位置的值
		 *
		 * @param key
		 * @param index 位置
		 * @return 值
		 **/
		public String lindex(String key, int index) {
			return SafeEncoder.encode(lindex(SafeEncoder.encode(key), index));
		}

		/**
		 * 获取List中指定位置的值
		 *
		 * @param key
		 * @param index 位置
		 * @return 值
		 **/
		public byte[] lindex(byte[] key, int index) {
			Jedis sjedis = getJedis();
			byte[] value = sjedis.lindex(key, index);
			closeJedis(sjedis);
			return value;
		}

		/**
		 * 将List中的第一条记录移出List
		 *
		 * @param key
		 * @return 移出的记录
		 */
		public String lpop(String key) {
			return SafeEncoder.encode(lpop(SafeEncoder.encode(key)));
		}

		/**
		 * 将List中的第一条记录移出List
		 *
		 * @param key
		 * @return 移出的记录
		 */
		public byte[] lpop(byte[] key) {
			Jedis jedis = getJedis();
			byte[] value = jedis.lpop(key);
			closeJedis(jedis);
			return value;
		}

		/**
		 * 将List中最后第一条记录移出List
		 *
		 * @param key
		 * @return 移出的记录
		 */
		public String rpop(String key) {
			Jedis jedis = getJedis();
			String value = jedis.rpop(key);
			closeJedis(jedis);
			return value;
		}

		/**
		 * 向List头部追加记录
		 *
		 * @param key
		 * @param value
		 * @return 记录总数
		 */
		public long lpush(String key, String value) {
			return lpush(SafeEncoder.encode(key), SafeEncoder.encode(value));
		}

		/**
		 * 向List尾部追加记录
		 *
		 * @param key
		 * @param value
		 * @return 记录总数
		 */
		public long rpush(String key, String value) {
			Jedis jedis = getJedis();
			long count = jedis.rpush(key, value);
			closeJedis(jedis);
			return count;
		}

		/**
		 * 向List尾部追加记录
		 *
		 * @param key
		 * @param value
		 * @return 记录总数
		 */
		public long rpush(byte[] key, byte[] value) {
			Jedis jedis = getJedis();
			long count = jedis.rpush(key, value);
			closeJedis(jedis);
			return count;
		}

		/**
		 * 向List头部追加记录
		 *
		 * @param key
		 * @param value
		 * @return 记录总数
		 */
		public long lpush(byte[] key, byte[] value) {
			Jedis jedis = getJedis();
			long count = jedis.lpush(key, value);
			closeJedis(jedis);
			return count;
		}

		/**
		 * 获取指定范围的记录，可以做为分页使用
		 *
		 * @param key
		 * @param start
		 * @param end
		 * @return List
		 */
		public List<String> lrange(String key, long start, long end) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			List<String> list = sjedis.lrange(key, start, end);
			closeJedis(sjedis);
			return list;
		}

		/**
		 * 获取指定范围的记录，可以做为分页使用
		 *
		 * @param key
		 * @param start
		 * @param end   如果为负数，则尾部开始计算
		 * @return List
		 */
		public List<byte[]> lrange(byte[] key, int start, int end) {
			//ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			List<byte[]> list = sjedis.lrange(key, start, end);
			closeJedis(sjedis);
			return list;
		}

		/**
		 * 删除List中c条记录，被删除的记录值为value
		 *
		 * @param key
		 * @param c     要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
		 * @param value 要匹配的值
		 * @return 删除后的List中的记录数
		 */
		public long lrem(byte[] key, int c, byte[] value) {
			Jedis jedis = getJedis();
			long count = jedis.lrem(key, c, value);
			closeJedis(jedis);
			return count;
		}

		/**
		 * 删除List中c条记录，被删除的记录值为value
		 *
		 * @param key
		 * @param c     要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
		 * @param value 要匹配的值
		 * @return 删除后的List中的记录数
		 */
		public long lrem(String key, int c, String value) {
			return lrem(SafeEncoder.encode(key), c, SafeEncoder.encode(value));
		}

		/**
		 * 算是删除吧，只保留start与end之间的记录
		 *
		 * @param key
		 * @param start 记录的开始位置(0表示第一条记录)
		 * @param end   记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
		 * @return 执行状态码
		 */
		public String ltrim(byte[] key, int start, int end) {
			Jedis jedis = getJedis();
			String str = jedis.ltrim(key, start, end);
			closeJedis(jedis);
			return str;
		}

		/**
		 * 算是删除吧，只保留start与end之间的记录
		 *
		 * @param key
		 * @param start 记录的开始位置(0表示第一条记录)
		 * @param end   记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
		 * @return 执行状态码
		 */
		public String ltrim(String key, int start, int end) {
			return ltrim(SafeEncoder.encode(key), start, end);
		}
	}

	//*******************************************Bitmap*******************************************//
	public class Bitmap {
		public Boolean setbit(String key, long index, boolean value){
			Jedis jedis = getJedis();
			Boolean result = jedis.setbit(key, index, value);
			closeJedis(jedis);
			return result;
		}
	}
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("1", 1);
		map.put("2", "sfasdfa");
		map.put("3", "sfawrwere3fa");

		String werwe = RedisUtil.getInstance().strings().set("sessionid", JSONObject.toJSONString(map));
		logger.info(werwe);
		String s = RedisUtil.getInstance().strings().get("sessionid");
		logger.info(s);

//		RedisUtil.getInstance().bitmap().setbit("money",9,true);
//
//		BitSet users = BitSet.valueOf(RedisUtil.getInstance().strings().get("money").getBytes());
//		int cardinality = users.cardinality();
//		System.out.println("total" + cardinality);
	}
}