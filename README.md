# shRIOL
This repository contains the OWL and SHACL files commented in the paper "Compliance checking in reified I/O logic via SHACL" submitted to ICAIL 2021.

The subfolder shRIOL contains Java files to execute the SHACL files on the ontology.

To compile: "javac -cp ./src/;./lib/* -d ./class ./src/DetectViolations.java"
To run compiled files: "java -cp ./class;./lib/* DetectViolations"

By executing the Java files, the following messages are printed on screen. See the paper for more details and explanations.

<b><i>
The model is not GDPR-compliant. The following violations have been detected:

Personal Data Processing: http://w3.org/ns/shRIOL#pdpHans<br>
MESSAGE: The personal data processing is not transparent, as required/defined by Article 12 of the GDPR<br>
EXPLANATION: Specifically, these legal authorities judged one or more communications related to pdpHans as follows:
<ul>
	<li>courtA does NOT deem the communication c2Hans enough readable.</li>
	<li>courtB deems the communication c2Hans enough readable.</li>
</ul>
Personal Data Processing: http://w3.org/ns/shRIOL#pdpLuca<br>
MESSAGE: The personal data processing is not lawful, as required by Art.5(1)(a) and defined by Art.6 of the GDPR.<br>
EXPLANATION: The age of the data subject is below the minimal age for consent in his/her Member State. See Art.8(1) of the GDPR.
</i></b>
