import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileUtils;
import org.topbraid.jenax.util.JenaUtil;

import org.topbraid.shacl.rules.RuleUtil;
import org.topbraid.shacl.validation.ValidationUtil;

public class DetectViolations 
{
    public static void main(String[] args) throws Exception 
    {
            //Load the ontology
        Model ontology = JenaUtil.createMemoryModel();
        FileInputStream fisOntology = new FileInputStream("./shRIOLontology.ttl");
        ontology.read(fisOntology, "urn:dummy", FileUtils.langTurtle);
        
            //Load the constitutive rules
        Model constitutiverules = JenaUtil.createMemoryModel();
        FileInputStream fisRules = new FileInputStream("./shRIOLconstitutiverules.shapes.ttl");
        constitutiverules.read(fisRules, "urn:dummy", FileUtils.langTurtle);
        
            //Load the obligations
        Model obligations = JenaUtil.createMemoryModel();
        FileInputStream fisShapes = new FileInputStream("./shRIOLobligations.shapes.ttl");
        obligations.read(fisShapes, "urn:dummy", FileUtils.langTurtle);
        
            //Some rules can trigger on results given by other rules. Thus, more inference iteractions are needed.
            //The inference engine of TopBraid composer iterates automatically. Java does not :-)
        Model oldInferredModel = ontology;
        Model inferredModel = RuleUtil.executeRules(oldInferredModel, constitutiverules, null, null).add(oldInferredModel);
        while(inferredModel.size()>oldInferredModel.size())
        {
            oldInferredModel = inferredModel;
            inferredModel = RuleUtil.executeRules(oldInferredModel, constitutiverules, null, null).add(oldInferredModel);
        }
        
            //Validate & Print
        Model report = ValidationUtil.validateModel(inferredModel, obligations, true).getModel();
        //System.out.println(org.topbraid.shacl.util.ModelPrinter.get().print(report));
        
        ArrayList<validationResult> validationResults = new ArrayList<validationResult>();
        boolean isCompliant = extractValidationResults.extract(report, validationResults);
        
        if(isCompliant==true)System.out.println("The model is GDPR compliant");
        else
        {
                //From the validation results, we try to build explanations (maybe there are exception or legal interpretations around).
            ArrayList<explanation> explanations = buildExplanations.buildExplanations(validationResults, inferredModel);
            
            System.out.println("\n\nThe model is not GDPR-compliant. The following violations have been detected:\n");
            for(explanation explanation:explanations)
            {
                System.out.println("----------------------------------------------------------------------------------------------");
                System.out.println("Personal Data Processing: "+explanation.instanceIRI);
                System.out.println("MESSAGE: "+explanation.message);
                System.out.println("EXPLANATION: "+explanation.explanation);
            }
            System.out.println("----------------------------------------------------------------------------------------------");
        }
    }
}