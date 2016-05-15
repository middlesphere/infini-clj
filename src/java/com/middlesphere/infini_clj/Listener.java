package com.middlesphere.infini_clj;

import org.infinispan.client.hotrod.annotation.*;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryExpiredEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryModifiedEvent;
import org.infinispan.client.hotrod.event.ClientCacheEntryRemovedEvent;


public class Listener {

    protected Handler handler;

    Listener(Handler handler) {
        this.handler = handler;
    }

    @ClientListener
    public static class CacheEntryCreated extends Listener {
        public CacheEntryCreated(Handler handler) {
            super(handler);
        }
        @ClientCacheEntryCreated
        public void handle(ClientCacheEntryCreatedEvent e) {
            handler.handle(e);
        }
    }

    @ClientListener
    public static class CacheEntryModified extends Listener {
        public CacheEntryModified(Handler handler) {
            super(handler);
        }
        @ClientCacheEntryModified
        public void handle(ClientCacheEntryModifiedEvent e) {
            handler.handle(e);
        }
    }

    @ClientListener
    public static class CacheEntryRemoved extends Listener {
        public CacheEntryRemoved(Handler handler) {
            super(handler);
        }
        @ClientCacheEntryRemoved
        public void handle(ClientCacheEntryRemovedEvent e) {
            handler.handle(e);
        }
    }

    @ClientListener
    public static class CacheEntryExpired extends Listener {
        public CacheEntryExpired(Handler handler) {
            super(handler);
        }
        @ClientCacheEntryExpired
        public void handle(ClientCacheEntryExpiredEvent e) {
            handler.handle(e);
        }
    }

    //Unfortunately didn't find evicted event

}

