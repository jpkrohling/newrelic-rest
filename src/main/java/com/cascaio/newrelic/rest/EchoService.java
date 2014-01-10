package com.cascaio.newrelic.rest;

import com.newrelic.api.agent.Trace;

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
    @Trace(dispatcher = true)
    public String get() {
        return "echo echo echo";
    }
}
