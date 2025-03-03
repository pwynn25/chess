package handler;

import com.google.gson.Gson;
import request.ClearRequest;
import result.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
   //    convert to an object
    // clear request most likely
    private ClearService clearService;

    public ClearHandler (ClearService clearService) {
        this.clearService = clearService;
    }

    public Object clearDB(Request req, Response res) {
//        ClearRequest request = new Gson().fromJson(req.body(), ClearRequest.class);

        ClearResult result = clearService.clear();
        res.status(200);

        return new Gson().toJson(result);
    }


}
