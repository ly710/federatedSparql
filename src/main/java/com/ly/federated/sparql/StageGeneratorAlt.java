package com.ly.federated.sparql;

import org.apache.commons.collections4.iterators.ArrayIterator;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.BasicPattern;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.ExecutionContext;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.binding.BindingBuilder;
import org.apache.jena.sparql.engine.binding.BindingRoot;
import org.apache.jena.sparql.engine.iterator.QueryIterPlainWrapper;
import org.apache.jena.sparql.engine.main.StageGenerator;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;

import java.util.ArrayList;
import java.util.List;

public class StageGeneratorAlt implements StageGenerator
{
    StageGenerator other ;

    public StageGeneratorAlt(StageGenerator other)
    {
        this.other = other ;
    }

    @Override
    public QueryIterator execute(BasicPattern pattern, QueryIterator input, ExecutionContext execCxt) {
        QueryExecutionHTTP queryExecutionHTTP = QueryExecutionHTTP.service("http://localhost:7200/repositories/test1", "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX ub: <http://example.com#>\n" +
                "SELECT ?X\t\n" +
                "WHERE\n" +
                "{ \n" +
                "  ?X rdf:type ub:GraduateStudent .\n" +
                "  ?X ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse0>\n" +
                "}");

        ResultSet resultSet = queryExecutionHTTP.execSelect();

        List<Binding> bindings = new ArrayList<>();
        while(resultSet.hasNext()) {
            QuerySolution querySolution = resultSet.next();
            for (String resultVar : resultSet.getResultVars()) {
                BindingBuilder bindingBuilder = BindingBuilder.create(BindingRoot.create());
                Binding binding = bindingBuilder.reset().add(Var.alloc(resultVar), querySolution.get(resultVar).asNode()).build();
                bindings.add(binding);
            }
        }

        ArrayIterator<Binding> arrayIterator = new ArrayIterator<>(bindings.toArray());
        return QueryIterPlainWrapper.create(arrayIterator);
    }
}