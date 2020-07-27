package org.adrianwalker.codepointdistance.rest;

import static java.lang.String.format;
import java.util.Map;
import static java.util.Map.of;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.adrianwalker.codepointdistance.distance.PythagorasDistance;
import org.adrianwalker.codepointdistance.model.CodePoint;
import org.adrianwalker.codepointdistance.model.Postcode;
import org.adrianwalker.codepointdistance.service.DistanceService;

@Path("/codepoint")
public final class CodePointResource {

  private static final Logger LOGGER = Logger.getLogger(CodePointResource.class.getName());
  private static final String NOT_FOUND = "Not Found";

  private final DistanceService service;

  public CodePointResource(final DistanceService service) {

    this.service = service;
  }

  @GET
  @Consumes(MediaType.WILDCARD)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/distance/{fromPostcode}/{toPostcode}")
  public Response distance(
    @PathParam("fromPostcode")
    final String fromPostcode,
    @PathParam("toPostcode")
    final String toPostcode) {

    LOGGER.log(INFO, format(
      "fromPostcode = %s, toPostcode = %s",
      fromPostcode, toPostcode));

    Postcode formattedFromPostcode = new Postcode().setValue(fromPostcode).format();
    Postcode formattedToPostcode = new Postcode().setValue(toPostcode).format();

    LOGGER.log(INFO, format(
      "formattedFromPostcode = %s, formattedToPostcode = %s",
      formattedFromPostcode, formattedToPostcode));

    CodePoint fromCodePoint = service.read(formattedFromPostcode);
    CodePoint toCodePoint = service.read(formattedToPostcode);

    LOGGER.log(INFO, format(
      "fromCodePoint = %s, toCodePoint = %s",
      fromCodePoint, toCodePoint));

    if (null == fromCodePoint || null == toCodePoint) {
      return notFound(fromCodePoint, toCodePoint);
    }

    double distance = service.distance(
      PythagorasDistance.INSTANCE,
      fromCodePoint,
      toCodePoint);

    LOGGER.log(INFO, format("distance = %s", distance));

    return ok(fromCodePoint, toCodePoint, distance);
  }

  public Response ok(final CodePoint fromCodePoint, final CodePoint toCodePoint, final double distance) {

    Map<String, Object> response = of(
      "fromCodePoint", fromCodePoint,
      "toCodePoint", toCodePoint,
      "distance", distance
    );

    return Response.ok(response).build();
  }

  public Response notFound(final CodePoint fromCodePoint, final CodePoint toCodePoint) {

    Map<String, Object> response = of(
      "fromCodePoint", fromCodePoint == null ? NOT_FOUND : fromCodePoint,
      "toCodePoint", toCodePoint == null ? NOT_FOUND : toCodePoint
    );

    return Response.status(Response.Status.NOT_FOUND).entity(response).build();
  }
}
