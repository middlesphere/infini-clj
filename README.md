# infini-clj

This library is simple Clojure wrapper to work with remote Infinispan 8 cache.

##Usage

Add to your project.clj the following line:
```
    :dependencies [[com.middlesphere/infini-clj "0.1"]]
```

##Examples:

1. Connect to remote cache

```
    (def c (connect! "127.0.0.1:11222" "default"))

```
2. Add listeners:

```
    (def lc (add-listener! c (fn [k v] (println "new entry | key: " k " value: " v)) :entry-created))
    (def lm (add-listener! c (fn [k v] (println "modified entry | key: " k " value: " v)) :entry-modified))
    (def lr (add-listener! c (fn [k v] (println "removed entry | key: " k " value: " v)) :entry-removed))
    (def le (add-listener! c (fn [k v] (println "expired entry | key: " k " value: " v)) :entry-expired))
```

3. Put some values:

```
    (put-value c :a 2)
    (put-value c :b 3)
    (put-value c :a 4)
    (put-value c :d 5 5000) ;Put some values with TTL 5 sec
```

4. Get values:

```
    (get-value c :a)
```
5. List all keys in cache:

```
    (get-all-keys c)
```

6. Clear cache:

```
    (clear-cache c)
```

7. Disconnect from cache:

```
    (disconnect! c)
```

