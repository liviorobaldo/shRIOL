import org.apache.jena.rdf.model.*;
import java.util.*;

public class buildExplanations 
{
    public static ArrayList<explanation> buildExplanations(ArrayList<validationResult> validationResults, Model ontology)
    {
        ArrayList<explanation> ret = new ArrayList<explanation>();
        for(validationResult validationResult:validationResults)
            for(String message:validationResult.messages)
                if(message.indexOf("The personal data processing is not lawful")!=-1)
                    ret.add(buildExplanationNonLawful(ontology.getResource(validationResult.instanceIRI), message, ontology));
                else if(message.indexOf("The personal data processing is not transparent")!=-1)
                    ret.add(buildExplanationNonTransparent(ontology.getResource(validationResult.instanceIRI), message, ontology));
        return ret;
    }
    
    private static explanation buildExplanationNonLawful(Resource instance, String message, Model ontology)
    {
        explanation ret = new explanation();
        ret.instanceIRI = instance.getURI();
        ret.message = message;
        ret.explanation = "(DEFAULT EXPLANATION) No legal basis is defined for the personal data processing (see Art.6(1))";
        
            //This instruction give me all classes of the instance, i.e., all RDFNode(s) in rdf:type relation with instance.
        NodeIterator ni = ontology.listObjectsOfProperty(instance, ontology.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));
        while(ni.hasNext())
        {
            RDFNode OWLClass = ni.nextNode();
            if(OWLClass.toString().indexOf("shRIOL#exception")!=-1)
            {
                ret.explanation = ontology.listObjectsOfProperty(OWLClass.asResource(), ontology.getProperty("http://www.w3.org/2000/01/rdf-schema#comment")).nextNode().toString();
                break;
            }
        }

        return ret;
    }
    
    private static explanation buildExplanationNonTransparent(Resource instance, String message, Model ontology)
    {
        String instanceName = instance.toString();
        instanceName = instanceName.substring(instanceName.lastIndexOf("#")+1, instanceName.length());
        
        explanation ret = new explanation();
        ret.instanceIRI = instance.getURI();
        ret.message = message;
        ret.explanation = "(DEFAULT EXPLANATION) At least one legal authority does not deem the personal data processing as transparent.";
        
            //This instruction give me all classes of the instance, i.e., all RDFNode(s) in rdf:type relation with instance.
        ret.explanation = "Specifically, these legal authorities judged one or more communications related to "+instanceName+" as follows:";
        NodeIterator ni1 = ontology.listObjectsOfProperty(instance, ontology.getProperty("http://w3.org/ns/shRIOL#is-theme-of"));    
        while(ni1.hasNext())
        {
            RDFNode Communication = ni1.nextNode();
            String CommunicationID = Communication.toString();
            CommunicationID = CommunicationID.substring(CommunicationID.lastIndexOf("#")+1, CommunicationID.length());
            
            NodeIterator ni2 = ontology.listObjectsOfProperty(Communication.asResource(), ontology.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));
            while(ni2.hasNext())
            {
                RDFNode OWLClass = ni2.nextNode();
                if(OWLClass.toString().indexOf("shRIOL#AssumedClear")!=-1)
                {
                    NodeIterator ni3 = ontology.listObjectsOfProperty(Communication.asResource(), ontology.getProperty("http://w3.org/ns/shRIOL#is-rejected-by"));
                    while(ni3.hasNext())
                    {
                        String LegalAuthority = ni3.nextNode().toString();
                        LegalAuthority = LegalAuthority.substring(LegalAuthority.lastIndexOf("#")+1, LegalAuthority.length());
                        ret.explanation += "\n\t- "+LegalAuthority+" does NOT deem the communication "+CommunicationID+" enough readable.";
                    }
                    
                    NodeIterator ni4 = ontology.listObjectsOfProperty(Communication.asResource(), ontology.getProperty("http://w3.org/ns/shRIOL#is-supported-by"));
                    while(ni4.hasNext())
                    {
                        String LegalAuthority = ni4.nextNode().toString();
                        LegalAuthority = LegalAuthority.substring(LegalAuthority.lastIndexOf("#")+1, LegalAuthority.length());
                        ret.explanation += "\n\t- "+LegalAuthority+" deems the communication "+CommunicationID+" enough readable.";
                    }
                }
            }
        }
        
        return ret;
    }
}
