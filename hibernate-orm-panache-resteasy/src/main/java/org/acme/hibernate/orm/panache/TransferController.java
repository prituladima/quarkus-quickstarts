package org.acme.hibernate.orm.panache;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Objects;

@Path("transfer")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferController {

    @POST
    @Transactional
    public Response create(Transfer transfer) {
        if(Objects.isNull(transfer.fromHolderId)){
            throw new WebApplicationException("Account holder from is not specified", 404);
        }

        if(Objects.isNull(transfer.toHolderId)){
            throw new WebApplicationException("Account holder to is not specified", 404);
        }

        if(Objects.isNull(transfer.sum)){
            throw new WebApplicationException("Sum is not specified", 404);
        }

        if(transfer.sum <= 0){
            throw new WebApplicationException("Sum must be more than 0", 404);
        }
        AccountHolder from = AccountHolder.findById(transfer.fromHolderId);
        AccountHolder to = AccountHolder.findById(transfer.toHolderId);

        if(from.balance < transfer.sum){
            throw new WebApplicationException("Not enough money", 405);
        }

        from.balance -= transfer.sum;
        to.balance += transfer.sum;

        return Response.ok(Arrays.asList(from, to)).status(201).build();
    }

}
