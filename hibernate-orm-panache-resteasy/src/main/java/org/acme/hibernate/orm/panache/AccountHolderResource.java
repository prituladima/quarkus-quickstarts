package org.acme.hibernate.orm.panache;

import io.quarkus.panache.common.Sort;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Path("holders")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountHolderResource {

    @GET
    public List<AccountHolder> get() {
        return AccountHolder.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public AccountHolder getSingle(@PathParam Long id) {
        AccountHolder entity = AccountHolder.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Account holder with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(AccountHolder fruit) {
        if (fruit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        fruit.persist();
        return Response.ok(fruit).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public AccountHolder update(@PathParam Long id, AccountHolder fruit) {
        if (fruit.name == null) {
            throw new WebApplicationException("Account holder Name was not set on request.", 422);
        }

        AccountHolder entity = AccountHolder.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Account holder with id of " + id + " does not exist.", 404);
        }

        entity.name = fruit.name;
        entity.balance = fruit.balance;

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        AccountHolder entity = AccountHolder.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Account holder with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception exception) {
            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }
            return Response.status(code)
                    .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
                    .build();
        }

    }
}
