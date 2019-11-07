package tv.engine.classes

import grails.util.Holders
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import groovyx.net.http.Method
import org.grails.web.json.JSONObject
import tv.engine.exceptions.GetException
import tv.engine.exceptions.RecordNotFoundException
import tv.engine.exceptions.TableNotFoundException

class EngineClient {
    def grailsApplication = Holders.grailsApplication.mainContext.getBean('grailsApplication')

    private String getHostString(){
        return String.format("http%s://%s:%s",
                grailsApplication.config.getProperty('tv.engine.ssl') == true ? "s" : "",
                grailsApplication.config.getProperty('tv.engine.host'),
                grailsApplication.config.getProperty('tv.engine.port')
        )
    }

    def exists(String endpoint){
        return this.get(String.format("%s/e", endpoint)).get('exists')
    }

    def get(String endpoint = "/"){
        def uri = String.format("%s%s", this.getHostString(), endpoint)
        def status = 0
        def j
        try {
            def http = new HTTPBuilder(uri)
            http.request(Method.GET, "application/json") { req ->
                response.success = { resp, json ->
                    status = resp.status
                    assert status == 200
                    j = json
                }
            }
            return j
        } catch(HttpResponseException e){
            if (e.getResponse().status == 404){
                println(uri)
                throw new TableNotFoundException(e.getMessage())
            } else {
                throw new GetException(String.format("cannot reach [GET]: %s, code: %s, msg: %s", uri, e.getResponse().status, e.getMessage()))
            }
        }
    }

    def post(String endpoint = "/", JSONObject params){
        def uri = String.format("%s%s", this.getHostString(), endpoint)
        def status = 0
        def j
        try {
            def http = new HTTPBuilder(uri)
            http.request(Method.POST, "application/json") { req ->
                body = params
                response.success = { resp, json ->
                    status = resp.status
                    j = json
                }
            }
            return j
        } catch(HttpResponseException e){
            if (e.getResponse().status == 404){
                throw new TableNotFoundException()
            } else {
                throw new GetException(String.format("cannot reach [POST]: %s, code: %s, msg: %s", uri, e.getResponse().status, e.getMessage()))
            }
        }
    }

    def post(String endpoint = "/", params){
        def uri = String.format("%s%s", this.getHostString(), endpoint)
        def status = 0
        def j
        try {
            def http = new HTTPBuilder(uri)
            http.request(Method.POST, "application/json") { req ->
                body = params
                response.success = { resp, json ->
                    status = resp.status
                    j = json
                }
            }
            return j
        } catch(HttpResponseException e){
            if (e.getResponse().status == 404){
                throw new TableNotFoundException()
            } else {
                throw new GetException(String.format("cannot reach [POST]: %s, code: %s, msg: %s", uri, e.getResponse().status, e.getMessage()))
            }
        }
    }

    def put(String endpoint = "/", JSONObject params){
        def uri = String.format("%s%s", this.getHostString(), endpoint)
        def status = 0
        def j
        try {
            def http = new HTTPBuilder(uri)
            http.request(Method.PUT, "application/json") { req ->
                body = params
                response.success = { resp, json ->
                    status = resp.status
                    j = json
                }
            }
            return j
        } catch(HttpResponseException e){
            if (e.getResponse().status == 404){
                throw new TableNotFoundException()
            } else {
                throw new GetException(String.format("cannot reach [PUT]: %s, code: %s, msg: %s", uri, e.getResponse().status, e.getMessage()))
            }
        }
    }

    def put(String endpoint = "/", params){
        def uri = String.format("%s%s", this.getHostString(), endpoint)
        def status = 0
        def j
        try {
            def http = new HTTPBuilder(uri)
            http.request(Method.PUT, "application/json") { req ->
                body = params
                response.success = { resp, json ->
                    status = resp.status
                    j = json
                }
            }
            return j
        } catch(HttpResponseException e){
            if (e.getResponse().status == 404){
                throw new TableNotFoundException()
            } else {
                throw new GetException(String.format("cannot reach [PUT]: %s, code: %s, msg: %s", uri, e.getResponse().status, e.getMessage()))
            }
        }
    }


    def delete(String endpoint = "/"){
        def uri = String.format("%s%s", this.getHostString(), endpoint)
        def status = 0
        try {
            def http = new HTTPBuilder(uri)
            http.request(Method.DELETE, "application/json") { req ->
                response.success = { resp, json ->
                    status = resp.status
                }
            }
            if (status != 204){
                throw new RecordNotFoundException()
            }
            return true
        } catch(HttpResponseException e){
            if (e.getResponse().status == 404){
                throw new TableNotFoundException()
            } else {
                throw new GetException(String.format("cannot reach [PUT]: %s, code: %s, msg: %s", uri, e.getResponse().status, e.getMessage()))
            }
        }
    }
}
