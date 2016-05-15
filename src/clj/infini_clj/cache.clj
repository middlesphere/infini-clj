(ns infini-clj.cache
  (:gen-class)
  (:import
    (org.infinispan.client.hotrod RemoteCache RemoteCacheManager)
    (java.util.concurrent TimeUnit)
    (com.middlesphere.infini_clj Listener Handler Listener$CacheEntryCreated Listener$CacheEntryModified Listener$CacheEntryRemoved Listener$CacheEntryExpired)
    (org.infinispan.client.hotrod.event ClientEvent)
    (org.infinispan.client.hotrod.configuration ConfigurationBuilder)))


(defn connect!
  "connect to remote Infinispan cache using HotRod protocol (default 11222 port).
  servers is string in format host:port separated by comma. Example: 127.0.0.1:11222,192.168.99.100:11222.
  cache-name is a string name for cache. Default cache name - default.
  return RemoteCache instance."
  ^RemoteCache
  ([servers cache-name]
   (let [conf-builder (ConfigurationBuilder.)]
     (.addServers conf-builder servers)
     (let [remote-cache-manager (RemoteCacheManager. (.build conf-builder))
           remote-cache (.getCache remote-cache-manager cache-name)]
       remote-cache)))
  ([servers] (connect! servers "default")))

(defn disconnect!
  "disconnect from cache and release all resources"
  [^RemoteCache cache]
  (.stop (.getRemoteCacheManager cache)))

(defn get-value
  "return value from cache using given key or nil if absent"
  [^RemoteCache cache key]
  (get cache key))

(defn get-all-keys
  "Return set of all keys"
  [^RemoteCache cache]
  (.keySet cache))

(defn put-value
  "put value to cache using given key.
  if ttl present then value be in cache for given milliseconds.
   return nil new value or previous value."
  ([^RemoteCache cache key value] (.put cache key value))
  ([^RemoteCache cache key value ttl] (.put cache key value ttl TimeUnit/MILLISECONDS)))

(defn put-all
  "Add all the entries in the map to the cache.
   each entry will be inserted as separated key/value in cache."
  [^RemoteCache cache map-values]
  (.putAll cache map-values))

(defn contains-key?
  "check if key in cache. return true if success or false if not."
  [^RemoteCache cache key]
  (.containsKey cache key))

(defn contains-value?
  "check if value in cache.
  return true if success or false if not."
  [^RemoteCache cache value]
  (.containsValue cache value))

(defn get-all
  "return the entries as map for the given keys. keys should be ^Set"
  [^RemoteCache cache keys]
  (.getAll cache keys))

(defn get-in-value
  "get cache value from nested structure described in vector [main-key nested-key1 nested-key2...]
  return value if success or nil"
  [^RemoteCache cache in-vec]
  (get-in (get-value cache (first in-vec)) (into [] (rest in-vec))))

(defn put-if-absent
  "If the specified key is not already associated with a value, associate it with the given value
  return nil if key is not present or value if key already in cache."
  [^RemoteCache cache key value]
  (.putIfAbsent cache key value))

(defn remove-key
  "remove K/V object in cache. return value if succes or nil."
  [^RemoteCache cache key]
  (.remove cache key))

(defn clear-cache
  "clear cache."
  [^RemoteCache cache]
  (.clear cache))

(defn add-listener!
  "add listener to cache.
  f is a function with 2 params [key new-value].
  f will be executed on host of remote client (not on cache servers).
  event-type is one of :entry-created :entry-modified :entry-removed :entry-expired.
  return Listener object which can be used in remove-listener!"
  [^RemoteCache cache f event-type]
  (let [body-impl (reify Handler
                    (handle [this entry]
                      (let [entry-key (.getKey entry)]
                        (f entry-key (get-value cache entry-key)))))
        listener (condp = event-type
                       :entry-created (Listener$CacheEntryCreated. body-impl)
                       :entry-modified (Listener$CacheEntryModified. body-impl)
                       :entry-removed (Listener$CacheEntryRemoved. body-impl)
                       :entry-expired (Listener$CacheEntryExpired. body-impl))]
    (.addClientListener cache listener)
    listener))

(defn remove-listener!
  "remove listener from cache.
  listener-obj is object returned by add-listener!"
  [^RemoteCache c listener-obj]
  (.removeClientListener c listener-obj))


