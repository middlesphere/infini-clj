package com.middlesphere.infini_clj;

/**
 * Created by mike on 03.05.16.
 */

import org.infinispan.client.hotrod.event.ClientEvent;

public interface Handler {
    void handle(ClientEvent e);
}