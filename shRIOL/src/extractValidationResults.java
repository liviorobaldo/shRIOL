import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import java.util.*;

public class extractValidationResults 
{
    public static boolean extract(Model report, ArrayList<validationResult> validationResults)
    {
        Resource ValidationReport = report.listResourcesWithProperty
            (
                report.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), 
                report.getResource("http://www.w3.org/ns/shacl#ValidationReport")
            ).nextResource();
        
        Property conformsProperty = report.getProperty("http://www.w3.org/ns/shacl#conforms");
        NodeIterator ni = report.listObjectsOfProperty(ValidationReport, conformsProperty);
        String value = ni.next().toString();
        if(value.indexOf("true")==0)return true;
        
        
            //If we are here, conforms=false and there are violations. Each violation is described in a result connected to the Resource "report".
        Property resultProperty = report.getProperty("http://www.w3.org/ns/shacl#result");
        
            //The Hashtable is only needed to add multiple messages to the same focusNodeIRI
        Hashtable<String,validationResult> ht = new Hashtable<String,validationResult>();
        ni = report.listObjectsOfProperty(ValidationReport, resultProperty);
        while(ni.hasNext())
        {
            RDFNode ValidationResult = ni.nextNode();
            Property focusNodeProperty = report.getProperty("http://www.w3.org/ns/shacl#focusNode");
            String focusNodeIRI = report.listObjectsOfProperty(ValidationResult.asResource(), focusNodeProperty).nextNode().toString();
            
            validationResult validationResult = ht.get(focusNodeIRI);
            if(validationResult==null)
            {
                    //If the focusNodeIRI does not have a validationResult yet, we create one and we add it both to the Hashtable and the returning ArrayList.
                validationResult = new validationResult();
                validationResult.instanceIRI = focusNodeIRI;
                validationResults.add(validationResult);
                ht.put(focusNodeIRI, validationResult);
            }
           
                //We check it if is a violation.
            Property resultSeverityProperty = report.getProperty("http://www.w3.org/ns/shacl#resultSeverity");
            if(report.listObjectsOfProperty(ValidationResult.asResource(), resultSeverityProperty).nextNode().toString().indexOf("#Violation")==-1)continue;
            
            Property resultResultMessage = report.getProperty("http://www.w3.org/ns/shacl#resultMessage");
            validationResult.messages.add(report.listObjectsOfProperty(ValidationResult.asResource(), resultResultMessage).nextNode().toString());
        }
        
        return false;
    }
}
