package com.seera.elaaoverlaypdf.service;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
/**
 * 
 * @author ricardopaulo
 *
 */
@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(OverlayDocumentService.class);
        register(MultiPartFeature.class);
    }
}