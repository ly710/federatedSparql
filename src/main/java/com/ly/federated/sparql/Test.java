package com.ly.federated.sparql;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.engine.main.StageGenerator;
import org.apache.jena.sparql.util.QueryExecUtils;

public class Test {
    public static void main(String[] args) {
        StageGenerator origStageGen = ARQ.getContext().get(ARQ.stageGenerator) ;
        StageGenerator stageGenAlt = new StageGeneratorAlt(origStageGen) ;
        ARQ.getContext().set(ARQ.stageGenerator, stageGenAlt) ;

        Query query = QueryFactory.create( String.join("\n", "")) ;
        QueryExecution engine = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel()) ;

        QueryExecUtils.executeQuery(query, engine) ;
    }
}
