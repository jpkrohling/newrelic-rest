package com.cascaio.newrelic.rest;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author jpkroehling
 *         2014-01-03
 */
@Stateless
@Path("/echo")
public class EchoService {
    @GET
    public String get() {
        return "echo echo echo";
    }
}
