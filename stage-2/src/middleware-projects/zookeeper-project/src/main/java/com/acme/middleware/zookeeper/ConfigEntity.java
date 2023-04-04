package com.acme.middleware.zookeeper;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * todo
 *
 * @author qian.he
 * @since 2023-04-04
 * @version 1.0.0
 */
public class ConfigEntity {

    public static final String VERSION = "0.1";

    private Header header;

    private String body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static class Header {

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @JsonProperty("content-type")
        private String contentType;

    }
}
