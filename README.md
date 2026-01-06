# shRIOL
<!-- This repository contains the OWL and SHACL files commented in the paper "Compliance checking in reified I/O logic via SHACL" submitted to ICAIL 2021. -->

More general than classes and instances, SHACL introduces the notion of shapes
that can formally describe constraints on the structure of RDF nodes and edges. The RDF graphs that satisfy the constraints denoted by the SHACL shapes are said to be valid with respect to those shapes.

The subfolder shRIOL contains Java files to execute the SHACL files on the OWL ontology.

To compile the Java files: "javac -cp ./src/;./lib/* -d ./class ./src/DetectViolations.java"<br>
To run compiled class files: "java -cp ./class;./lib/* DetectViolations"

By executing the Java files, the following messages are printed on screen. See the paper for more details and explanations.

<pre>
The model is not GDPR-compliant. The following violations have been detected:
----------------------------------------------------------------------------------------------
Personal Data Processing: http://w3.org/ns/shRIOL#pdpHans
MESSAGE: The personal data processing is not transparent, as required/defined by Article 12 of the GDPR
EXPLANATION: Specifically, these legal authorities judged one or more communications related to pdpHans as follows:
	- courtA does NOT deem the communication c2Hans enough readable.
	- courtB deems the communication c2Hans enough readable.
----------------------------------------------------------------------------------------------
Personal Data Processing: http://w3.org/ns/shRIOL#pdpLuca
MESSAGE: The personal data processing is not lawful, as required by Art.5(1)(a) and defined by Art.6 of the GDPR.
EXPLANATION: The age of the data subject is below the minimal age for consent in his/her Member State. See Art.8(1) of the GDPR.
----------------------------------------------------------------------------------------------
</pre>
